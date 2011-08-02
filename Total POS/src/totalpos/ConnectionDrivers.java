package totalpos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shidalgo
 */
public class ConnectionDrivers {

    protected static ComboPooledDataSource cpds ;
    private static long lastOperationTime = Calendar.getInstance().getTimeInMillis();
    public static User user;

    /** Crea la piscina de conexiones.
     * 
     * @return Retorna un valor verdadero en caso de que
     * se inicialice correctamente la piscina de conexiones.
     */
    protected static boolean initialize(){
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Constants.dbHost + "/" +
                            Constants.dbName;
            cpds.setJdbcUrl(sT);
            cpds.setUser(Constants.dbUser);
            cpds.setPassword(Constants.dbPassword);

            return true;

        } catch (PropertyVetoException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "No se encontró el driver.",ex);
            msb.show(Main.splash);
            return false;
        }
        
    }

    /** Indica si una contraseña y usuario con correctos.
     *
     * @param user Usuario
     * @param password Contraseña
     * @throws SQLException Para problemas con la base de datos.
     * @throws Exception Para contraseña incorrecta.
     */
    protected static void login(String user, char[] password) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? and password = ? ");
        pstmt.setString(1, user);
        pstmt.setString(2, Shared.hashPassword(new String(password)));

        if ( ! pstmt.executeQuery().next() ){
            c.close();
            throw new Exception(Constants.wrongPasswordMsg);
        }

        lastOperationTime = Calendar.getInstance().getTimeInMillis();

        c.close();
    }

    /** Genera la lista de usuarios.
     *
     * @return Lista de usuarios
     * @throws SQLException Para problemas de conexión con la base de datos.
     * @throws Exception
     */
    protected static List<User> listUsers() throws SQLException, Exception{
        List<User> ans = new LinkedList<User>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select login, password, nombre, apellido, tipo_de_usuario_id, bloqueado,debeCambiarPassword,puedeCambiarPassword from usuario");

        while ( rs.next() ){
            ans.add(new User(rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("tipo_de_usuario_id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getInt("bloqueado"),
                    rs.getInt("debeCambiarPassword"),
                    rs.getInt("puedeCambiarPassword")));
        }

        c.close();
        return ans;
    }

    /** Dado un nodo, devuelve su predecesor.
     * 
     * @param e Identificador del nodo.
     * @return Predecesor.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static Edge getPredecesor(String e) throws SQLException, Exception{

        verifyIdle();
        if ( e == null ) {
            return null;
        }
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id, nombre, predecesor, icono, funcion from nodo where id = ? ");
        stmt.setString(1, e);
        
        ResultSet rs = stmt.executeQuery();

        if ( ! rs.next() ){
            c.close();
            return null;
        }

        Edge ee = new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion"));

        c.close();

        return ee;
    }

    /**
     * Crea un perfil.
     * @param id Identificador del perfil.
     * @param description Descripción del perfil
     * @throws SQLException Para problemas de conexión con la base de datos.
     * @throws Exception 
     */
    protected static void createProfile(String id, String description) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into tipo_de_usuario( id, descripcion ) values ( ? , ? )");
        stmt.setString(1, id);
        stmt.setString(2, description);
        stmt.executeUpdate();
        c.close();
    }

    /**
     * Devuelve una lista con todos los perfiles que coincidan con ID.
     * @param id Identificador del perfil
     * @return Lista de perfiles.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Profile> listProfile(String id) throws SQLException, Exception{
        verifyIdle();
        List<Profile> ans = new LinkedList<Profile>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion from tipo_de_usuario where id like ? ");
        stmt.setString(1, "%" + id + "%");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Profile(rs.getString("id"), rs.getString("descripcion")));
        }

        c.close();

        return ans;
    }

    /**
     * Dado un predecesor y un perfil, indica la lista de los sucesores en
     * los ese perfil tiene permisos para usarlo.
     * @param parent Predecesor.
     * @param profile Perfil.
     * @return Lista de sucesores.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Edge> listEdgesAllowed(String parent, String profile) throws SQLException, Exception{
        verifyIdle();
        List<Edge> ans = new LinkedList<Edge>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id,n.nombre,n.predecesor,n.icono,n.funcion "
                + "from nodo n,tipo_de_usuario_puede t "
                + "where n.predecesor= ? and t.id_tipo_usuario= ? and t.id_nodo=n.id  and " + ((Constants.isPos)?"punto_de_venta = 1":"administrativo = 1"));
        stmt.setString(1, parent);
        stmt.setString(2, profile);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion")));
        }

        c.close();
        return ans;
    }

    /**
     * Dado un predecesor, devuelve a todos los sucesores.
     * @param parent Predecesor.
     * @return Lista de Sucesores.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Edge> listEdges(String parent) throws SQLException, Exception{
        verifyIdle();
        List<Edge> ans = new LinkedList<Edge>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id,n.nombre,n.predecesor,n.icono,n.funcion "
                + "from nodo n "
                + "where n.predecesor = ? and " + ((Constants.isPos)?"punto_de_venta = 1":"administrativo = 1"));
        stmt.setString(1, parent);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion")));
        }

        c.close();

        return ans;
    }

    protected static boolean isAllowed(String profile, String id) throws SQLException, Exception{
        verifyIdle();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id "
                + "from nodo n, tipo_de_usuario_puede t "
                + "where n.id = ? and t.id_tipo_usuario = ? and t.id_nodo = n.id ");
        stmt.setString(1, id);
        stmt.setString(2, profile);

        boolean ans = stmt.executeQuery().next();
        
        c.close();
        
        return ans;
    }

    protected static void disableMenuProfile(String profile, String id) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from tipo_de_usuario_puede where id_tipo_usuario = ? and id_nodo = ?");
        stmt.setString(1, profile);
        stmt.setString(2, id);
        stmt.executeUpdate();
        
        c.close();
    }

    protected static void enableMenuProfile(String profile, String id) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into tipo_de_usuario_puede(id_tipo_usuario , id_nodo) values ( ? , ? )");
        stmt.setString(1, profile);
        stmt.setString(2, id);
        stmt.executeUpdate();

        c.close();
    }

    protected static void setPassword(String user, String password) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set password = ? where login = ? ");
        stmt.setString(1, password);
        stmt.setString(2, user);
        stmt.executeUpdate();

        c.close();
    }

    protected static void createUser(String username, String role, String password) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into usuario"
                + " ( login , password , tipo_de_usuario_id , bloqueado , nombre ) values ( ? , ? , ? , ? , ?)");
        stmt.setString(1, username);
        stmt.setString(2, Shared.hashPassword(password));
        stmt.setString(3, role);
        stmt.setInt(4, 0);
        stmt.setString(5, username);
        stmt.executeUpdate();

        c.close();
    }

    public static void changeProperties(String loginT, String nombreT,
            String apellidoT, String roleT,
            boolean bloqueado, boolean puede , boolean debe) throws SQLException, Exception {
        verifyIdle();
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set nombre = ? ,"
                + " apellido = ? , tipo_de_usuario_id = ? , bloqueado = ? , debeCambiarPassword = ? ,"
                + " puedeCambiarPassword = ? "
                + "where login = ? ");
        stmt.setString(1, nombreT);
        stmt.setString(2, apellidoT);
        stmt.setString(3, roleT);
        stmt.setInt(4, bloqueado?1:0);
        stmt.setInt(5, debe?1:0);
        stmt.setInt(6, puede?1:0);
        stmt.setString(7, loginT);
        stmt.executeUpdate();

        c.close();
    }

    public static boolean existsUser(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? ");
        pstmt.setString(1, username);

        if ( ! pstmt.executeQuery().next() ){
            c.close();
            return false;
        }

        c.close();
        return true;
    }

    public static boolean isLocked(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? and bloqueado = 1 ");
        pstmt.setString(1, username);

        if ( ! pstmt.executeQuery().next() ){
            c.close();
            return false;
        }

        c.close();
        return true;
    }

    public static void lockUser(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set bloqueado = 1 where login = ? ");
        stmt.setString(1, username);
        stmt.executeUpdate();
        c.close();
    }

    static void changeProfileDetails(String prevId, String id, String description) throws SQLException, Exception {
        verifyIdle();
        if ( prevId.equals(id) ){ //Caso trivial xD;
            Connection c = ConnectionDrivers.cpds.getConnection();
            PreparedStatement stmt = c.prepareStatement("update tipo_de_usuario set descripcion = ? where id = ? ");
            stmt.setString(1, description);
            stmt.setString(2, id);
            stmt.executeUpdate();
            return; 
        }

        createProfile(id, description);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update tipo_de_usuario_puede set id_tipo_usuario = ? where id_tipo_usuario = ? ");
        stmt.setString(1, id);
        stmt.setString(2, prevId);
        stmt.executeUpdate();

        stmt = c.prepareStatement("update usuario set tipo_de_usuario_id = ? where tipo_de_usuario_id = ? ");
        stmt.setString(1, id);
        stmt.setString(2, prevId);
        stmt.executeUpdate();

        deleteProfile(prevId);

        c.close();
    }

    private static void deleteProfile(String id) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from tipo_de_usuario where id = ? ");
        stmt.setString(1, id);
        stmt.executeUpdate();
        c.close();
    }

    public static void mustntChangePassword(String username) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set debeCambiarPassword = 0 where login = ? ");
        stmt.setString(1, username);
        stmt.executeUpdate();
        
        c.close();
    }

    private static void verifyIdle() throws SQLException, Exception{
        if ( user != null && Calendar.getInstance().getTimeInMillis() - lastOperationTime > Long.valueOf(Shared.getConfig("idleTime")) ){
             MessageBox msg = new MessageBox(MessageBox.SGN_WARNING, "El usuario ha permanecido mucho tiempo sin uso. Requiere contraseña.");
             msg.show(MainWindows.mw);
             PasswordNeeded pn = new PasswordNeeded(MainWindows.mw, true, user);
             Shared.centerFrame(pn);
             pn.setVisible(true);
             if ( pn.isPasswordOk() ){
                 lastOperationTime = Calendar.getInstance().getTimeInMillis();
             }else{
                 throw new Exception("No se realizó la operación. Contraseña Incorrecta.");
             }
        }else{
            lastOperationTime = Calendar.getInstance().getTimeInMillis();
        }
    }

    protected static void initializeConfig(){
        try {
            Shared.config.clear();
            Connection c = ConnectionDrivers.cpds.getConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select `Key` , `Value` from configuracion");
            while (rs.next()) {
                Shared.config.put(rs.getString("Key"), rs.getString("Value"));
            }
            c.close();
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "Problemas con la base de datos.", ex);
            msb.show(MainWindows.mw);
        }
    }

    protected static void saveConfig(String k, String v) throws SQLException, Exception{
        verifyIdle();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update configuracion set `value` = ? where `key` = ? ");
        stmt.setString(1, v);
        stmt.setString(2, k);
        stmt.executeUpdate();
        c.close();
    }

    private static List<Cost> listCosts(String code) throws SQLException{
        List<Cost> ans = new LinkedList<Cost>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement( "select monto , fecha from costo where codigo_de_articulo = ? " );
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ans.add(new Cost(rs.getDate("fecha"), rs.getDouble("monto")));
        }
        c.close();

        return ans;
    }

    private static List<Price> listPrices(String code) throws SQLException{
        List<Price> ans = new LinkedList<Price>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement( "select monto , fecha from precio where codigo_de_articulo = ? " );
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ans.add(new Price(rs.getDate("fecha")
                    , (double)Math.round(rs.getDouble("monto")*(Double.valueOf(Shared.config.get("iva"))+1.0))));
        }
        c.close();

        return ans;
    }

    private static List<String> listBarcodes(String code) throws SQLException{
        List<String> ans = new LinkedList<String>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement( "select codigo_de_barras from codigo_de_barras where codigo_de_articulo = ? " );
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ans.add(rs.getString("codigo_de_barras"));
        }
        c.close();

        return ans;
    }

    protected static List<Item> listItems(String barCode, String code, String description, String model) throws SQLException, Exception{
        verifyIdle();
        List<Item> ans = new LinkedList<Item>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento "
                + "from articulo a "
                + "where a.codigo like ? and a.descripcion like ? and a.modelo like ? and "
                + "((exists  (select * from codigo_de_barras where codigo_de_barras.codigo_de_articulo = a.codigo "
                + "and codigo_de_barras.codigo_de_barras like ? "
                + ") ) or a.codigo_de_barras like ? )");
        stmt.setString(1, "%" + code + "%");
        stmt.setString(2, "%" + description + "%");
        stmt.setString(3, "%" + model + "%");
        stmt.setString(4, "%" + barCode + "%");
        stmt.setString(5, "%" + barCode + "%");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Item(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_registro"),
                        rs.getString("marca"),
                        rs.getString("sector"),
                        rs.getString("codigo_sublinea"),
                        rs.getString("codigo_de_barras"),
                        rs.getString("modelo"),
                        rs.getString("unidad_venta"),
                        rs.getString("unidad_compra"),
                        rs.getInt("existencia_actual"),
                        listPrices(rs.getString("codigo")),
                        listCosts(rs.getString("codigo")),
                        listBarcodes(rs.getString("codigo")),
                        rs.getBoolean("bloqueado"),
                        rs.getString("imagen"),
                        rs.getString("descuento")
                        )
                    );
        }
        c.close();

        return ans;
    }

    protected static String getIdProfile(String name) throws SQLException, Exception{
        verifyIdle();

        if ( name.equals("/") ) {
            return "root";
        }

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id from nodo where nombre = ? ");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        String ans = rs.getString("id");
        c.close();
        return ans;
    }

    protected static List<User> listRetailUsers() throws SQLException, Exception{
        List<User> u = listUsers();
        List<User> ans = new ArrayList<User>();

        for (User us : u) {
            if (isAllowed(us.getPerfil(), "retail")) {
                ans.add(us);
            }
        }

        return ans;
    }

    protected static List<String> listPOS() throws SQLException, Exception{
        verifyIdle();

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select identificador from punto_de_venta");

        List<String> ans = new ArrayList<String>();
        while ( rs.next() ) {
            String id = rs.getString("identificador");
            ans.add(id);
        }

        return ans;
    }

    private static boolean existTurnOpenFor(String username) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(" select * from turno "
                + "where datediff(now(),fecha) = 0 and codigo_de_usuario = ? and estado = 'Abierto'");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();
        c.close();

        return ans;
    }

    protected static void createTurn(String user, String comp, double cash) throws SQLException, Exception{
        verifyIdle();
        if ( existTurnOpenFor(user) ){
            throw new Exception(Constants.dataRepeated);
        }

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into turno"
                + " ( codigo_de_usuario , codigo_de_pos , fecha , efectivo_en_caja , estado ) "
                + "values ( ? , ? , now() , ? , 'Abierto')");
        stmt.setString(1, user);
        stmt.setString(2, comp);
        stmt.setDouble(3, cash);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Turn> listTurnsToday() throws SQLException{
        List<Turn> ans = new ArrayList<Turn>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select codigo_de_usuario , codigo_de_pos , fecha, efectivo_en_caja , estado from turno "
                + "where datediff(now(),fecha) = 0");

        while ( rs.next() ) {
            ans.add(
                    new Turn(
                        rs.getString("codigo_de_usuario"),
                        rs.getString("codigo_de_pos"),
                        rs.getDouble("efectivo_en_caja"),
                        rs.getString("estado").equals("Abierto"),
                        rs.getString("fecha"))
                    );
        }

        return ans;
    }

}
