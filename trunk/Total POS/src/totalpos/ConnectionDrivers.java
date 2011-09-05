package totalpos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author Saúl Hidalgo
 */
public class ConnectionDrivers {

    protected static ComboPooledDataSource cpds ;
    protected static ComboPooledDataSource cpdsMirror; // Just to mirror
    protected static boolean mirrorConnected = false;

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

    protected static boolean reinitializeOffline(){
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Constants.mirrorDbHost + "/" +
                            Constants.mirrorDbName;
            cpds.setJdbcUrl(sT);
            cpds.setUser(Constants.mirrordbUser);
            cpds.setPassword(Constants.mirrordbPassword);

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
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            c.close();
            rs.close();
            throw new Exception(Constants.wrongPasswordMsg);
        }
        c.close();
        rs.close();
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
        rs.close();
        return ans;
    }

    /** Dado un nodo, devuelve su predecesor.
     * 
     * @param e Identificador del nodo.
     * @return Predecesor.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static Edge getPredecesor(String e) throws SQLException, Exception{

        if ( e == null ) {
            return null;
        }
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id, nombre, predecesor, icono, funcion from nodo where id = ? ");
        stmt.setString(1, e);
        
        ResultSet rs = stmt.executeQuery();

        if ( ! rs.next() ){
            rs.close();
            c.close();
            return null;
        }

        Edge ee = new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion"));

        c.close();
        rs.close();

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
        List<Profile> ans = new LinkedList<Profile>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion from tipo_de_usuario where id like ? ");
        stmt.setString(1, "%" + id + "%");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Profile(rs.getString("id"), rs.getString("descripcion")));
        }

        c.close();
        rs.close();

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
        rs.close();
        return ans;
    }

    /**
     * Dado un predecesor, devuelve a todos los sucesores.
     * @param parent Predecesor.
     * @return Lista de Sucesores.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Edge> listEdges(String parent) throws SQLException, Exception{
        List<Edge> ans = new LinkedList<Edge>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id,n.nombre,n.predecesor,n.icono,n.funcion "
                + "from nodo n "
                + "where n.predecesor = ? " /*and " + ((Constants.isPos)?"punto_de_venta = 1":"administrativo = 1")*/);
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
        rs.close();

        return ans;
    }

    protected static boolean isAllowed(String profile, String id) throws SQLException{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id "
                + "from nodo n, tipo_de_usuario_puede t "
                + "where n.id = ? and t.id_tipo_usuario = ? and t.id_nodo = n.id ");
        stmt.setString(1, id);
        stmt.setString(2, profile);

        ResultSet rs = stmt.executeQuery();
        boolean ans = rs.next();
        
        c.close();
        rs.close();
        
        return ans;
    }

    protected static void disableMenuProfile(String profile, String id) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from tipo_de_usuario_puede where id_tipo_usuario = ? and id_nodo = ?");
        stmt.setString(1, profile);
        stmt.setString(2, id);
        stmt.executeUpdate();
        
        c.close();
    }

    protected static void enableMenuProfile(String profile, String id) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into tipo_de_usuario_puede(id_tipo_usuario , id_nodo) values ( ? , ? )");
        stmt.setString(1, profile);
        stmt.setString(2, id);
        stmt.executeUpdate();

        c.close();
    }

    protected static void setPassword(String user, String password) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set password = ? where login = ? ");
        stmt.setString(1, password);
        stmt.setString(2, user);
        stmt.executeUpdate();

        c.close();
    }

    protected static void createUser(String username, String role, String password) throws SQLException, Exception{
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
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            rs.close();
            c.close();
            return false;
        }

        c.close();
        rs.close();
        return true;
    }

    public static boolean isLocked(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? and bloqueado = 1 ");
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            c.close();
            rs.close();
            return false;
        }

        c.close();
        rs.close();
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
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set debeCambiarPassword = 0 where login = ? ");
        stmt.setString(1, username);
        stmt.executeUpdate();
        
        c.close();
    }

    protected static void initializeConfig() throws SQLException{
        Shared.getConfig().clear();
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select `Key` , `Value` from configuracion");
        while (rs.next()) {
            Shared.getConfig().put(rs.getString("Key"), rs.getString("Value"));
        }
        c.close();
        rs.close();
    }

    protected static void saveConfig(String k, String v) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from configuracion where `key` = ? ");
        stmt.setString(1, k);
        stmt.executeUpdate();

        stmt = c.prepareStatement("insert into configuracion (`key`,`value`) values (?,?) ");
        stmt.setString(1, k);
        stmt.setString(2, v);
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
        rs.close();

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
                    , (rs.getDouble("monto"))));
        }
        c.close();
        rs.close();

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
        rs.close();

        return ans;
    }

    protected static List<Item> listItems(String barCode, String code, String description, String model) throws SQLException, Exception{
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
        rs.close();

        return ans;
    }

    protected static String getIdProfile(String name) throws SQLException, Exception{

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
        rs.close();
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

    protected static List<PointOfSale> listPOS() throws SQLException{

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select identificador, descripcion, impresora, habilitada from punto_de_venta");

        List<PointOfSale> ans = new ArrayList<PointOfSale>();
        while ( rs.next() ) {
            ans.add( new PointOfSale(
                    rs.getString("identificador"),
                    rs.getString("descripcion"),
                    rs.getString("impresora"),
                    rs.getBoolean("habilitada")) );
        }
        c.close();
        rs.close();

        return ans;
    }

    /**
     * Deprecated
     * Now turns are associated to pos.
     * @param username
     * @return
     * @throws SQLException
     */
    private static boolean existTurnOpenFor(String username) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(" select * from turno "
                + "where datediff(now(),fecha) = 0 and codigo_de_usuario = ? and estado = 'Abierto'");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();
        c.close();
        rs.close();

        return ans;
    }

    protected static void createTurn(String id, String description, Time a, Time b) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into turno"
                + " ( identificador , nombre , inicio, fin ) "
                + "values ( ? , ? , ? , ? )");
        stmt.setString(1, id);
        stmt.setString(2, description);
        stmt.setTime(3, a);
        stmt.setTime(4, b);
        stmt.executeUpdate();

        c.close();
    }

    protected static void modifyTurn(String id, String description, Time a, Time b) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update turno set "
                + " nombre = ? , inicio = ? , fin = ? where Identificador = ?");
        stmt.setString(1, description);
        stmt.setTime(2, a);
        stmt.setTime(3, b);
        stmt.setString(4, id);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Turn> listTurns() throws SQLException{
        List<Turn> ans = new ArrayList<Turn>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "select identificador , nombre , inicio, fin from turno ");

        while ( rs.next() ) {
            ans.add(
                    new Turn(
                        rs.getString("identificador"),
                        rs.getString("nombre"),
                        rs.getTime("inicio"),
                        rs.getTime("fin"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    private static void changeItemStock( String id, int quant ) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update articulo set existencia_actual = existencia_actual +" + quant + " where codigo = ? ");
        stmt.setString(1, id);
        stmt.executeUpdate();
        c.close();
    }

    protected static void createReceipt(String id, String user , Assign assign) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into factura"
                + " ( codigo_interno, estado, fecha_creacion , total_sin_iva , total_con_iva , iva, codigo_de_usuario, cantidad_de_articulos , codigo_de_cliente , identificador_turno , identificador_pos) "
                + "values ( ? , 'Pedido' , now() , 0, 0, 0 , ? , 0 , \"Contado\", ? , ?)");
        stmt.setString(1, id);
        stmt.setString(2, user);
        stmt.setString(3, assign.getTurn());
        stmt.setString(4, assign.getPos());
        stmt.executeUpdate();

        c.close();
    }

    protected static double accumulatedInReceipt(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select total_sin_iva from factura where codigo_interno = ?");
        stmt.setString(1, receiptId);
        ResultSet rs = stmt.executeQuery();

        Double ans = null;
        boolean ok = rs.next();
        assert(ok);
        ans = rs.getDouble("total_sin_iva");
        c.close();
        rs.close();

        return ans;
    }

    protected static void addItem2Receipt(String receiptId, Item item, int quant) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into factura_contiene"
                + " ( codigo_interno_factura, codigo_de_articulo, cantidad) "
                + "values ( ? , ? , ? )");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getCode());
        stmt.setInt(3, quant);
        stmt.executeUpdate();

        changeItemStock(item.getCode(), -1);

        double withoutTax = item.getLastPrice().getQuant()*quant;
        double subT = accumulatedInReceipt(receiptId) + withoutTax;
        stmt = c.prepareStatement("update factura "
                + "set total_sin_iva = " + (subT) +
                " , total_con_iva =" + (new Price(null,subT)).plusIva().getQuant() +
                " , iva = " + (new Price(null,subT)).getIva().getQuant() +
                " , cantidad_de_articulos = cantidad_de_articulos + 1 "
                + "where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();
        
        c.close();

    }

    protected static void deleteItem2Receipt(String receiptId, Item item) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from factura_contiene where"
                + " codigo_interno_factura = ? and codigo_de_articulo = ? limit 1");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getCode());
        stmt.executeUpdate();

        changeItemStock(item.getCode(), 1);

        double withoutTax = -1*(item.getLastPrice().getQuant());
        double subT = accumulatedInReceipt(receiptId) - withoutTax;
        stmt = c.prepareStatement("update factura "
                + "set total_sin_iva = total_sin_iva + " + withoutTax +
                " , total_con_iva =" + (new Price(null,subT)).plusIva().getQuant() +
                " , iva = " + (new Price(null,subT)).getIva().getQuant() +
                " , cantidad_de_articulos = cantidad_de_articulos - 1 "
                + "where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();

    }

    protected static int lastReceiptToday() throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select count(*) from factura "
                + "where datediff(now(),fecha_creacion) = 0 and identificador_pos = ? ");
        stmt.setString(1, Constants.myId);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        int ans = rs.getInt(1);
        c.close();
        rs.close();

        return ans;
    }

    public static Date getDate() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select now()");

        boolean ok = rs.next();
        assert(ok);
        Date ans = rs.getDate(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static void createPos(String number, String local, String printer) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into punto_de_venta"
                + " ( identificador, descripcion, impresora , habilitada) "
                + "values ( ? , ? , ? , 1)");
        stmt.setString(1, number);
        stmt.setString(2, local);
        stmt.setString(3, printer);
        stmt.executeUpdate();

        c.close();
    }

    protected static void modifyPos(String number, String local, String printer) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set "
                + " descripcion = ? , impresora = ? where identificador = ? ");
        stmt.setString(1, local);
        stmt.setString(2, printer);
        stmt.setString(3, number);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Assign> listAssignsTurnPosToday() throws SQLException{
        List<Assign> ans = new ArrayList<Assign>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador_turno, "
                + "identificador_pos , fecha , abierto from asigna where datediff(fecha,now()) = 0");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new Assign(
                        rs.getString("identificador_turno"),
                        rs.getString("identificador_pos"),
                        rs.getDate("fecha"),
                        rs.getBoolean("abierto")
                       )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<PointOfSale> listPointOfSales(boolean enabled) throws SQLException{
        List<PointOfSale> ans = new ArrayList<PointOfSale>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador , descripcion , impresora , habilitada "
                + "from punto_de_venta where habilitada = ? ");
        stmt.setBoolean(1, enabled);

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new PointOfSale(
                        rs.getString("identificador"),
                        rs.getString("descripcion"),
                        rs.getString("impresora"),
                        rs.getBoolean("habilitada"))
                        );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static void createAssign(Assign a) throws SQLException{

        if ( ! assignIsOk(a) ){
            throw new SQLException(Constants.duplicatedMsg);
        }

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into asigna "
                + "(identificador_turno , identificador_pos , fecha , "
                + "abierto )  values ( ? , ? , now() , ? )");
        stmt.setString(1, a.getTurn());
        stmt.setString(2, a.getPos());
        stmt.setBoolean(3, a.isOpen());
        stmt.executeUpdate();

        c.close();
    }

    private static boolean isOverlapping(Assign a) throws SQLException{
        List<Assign> l = listAssignsTurnPosToday();
        Turn newTurn = Shared.getTurn(listTurns(), a.getTurn());
        for (Assign assign : l) {
            Turn t = Shared.getTurn(listTurns(), assign.getTurn());
            // Yo denominaría a esto como un if en Cascada :-o!
            if ( a.getPos().equals(assign.getPos()) &&
                    (a.getTurn().equals(assign.getTurn()) ||
                        ( (t.getInicio().before(newTurn.getFin()) && newTurn.getInicio().before(t.getFin())) ||
                            (newTurn.getInicio().before(t.getFin()) && t.getInicio().before(newTurn.getFin())) ) )){
                return true;
            }
        }
        return false;
    }

    private static boolean isExpired(Assign a) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Turn t = Shared.getTurn(listTurns(), a.getTurn());

        PreparedStatement stmt = c.prepareStatement("select curtime()");
        ResultSet rs = stmt.executeQuery();
        boolean ans = rs.next();
        assert(ans);
        ans = rs.getTime(1).before(t.getFin());

        c.close();
        return !ans;
    }

    private static boolean assignIsOk(Assign a) throws SQLException{
        return !isOverlapping(a) && !isExpired(a);
    }

    protected static List<Assign> listAssignsTurnPosRightNow() throws SQLException{
        List<Assign> ans = new ArrayList<Assign>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.identificador_turno, "
                + "a.identificador_pos , a.fecha , a.abierto "
                + "from asigna a , turno t "
                + "where datediff(fecha,now()) = 0 and "
                + "t.Identificador = a.identificador_turno and t.inicio <= now() and t.fin >= now()");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new Assign(
                        rs.getString("identificador_turno"),
                        rs.getString("identificador_pos"),
                        rs.getDate("fecha"),
                        rs.getBoolean("abierto"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    public static void putToIdle(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Espera' where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }

    public static void putToNormal(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Pedido' where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }


    public static void cancelReceipt(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Anulada' where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Item> listItems(String receiptID) throws SQLException{
        List<Item> ans = new ArrayList<Item>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento "
                + "from articulo a , factura_contiene fc where fc.codigo_interno_factura = ? and fc.codigo_de_articulo = a.codigo");
        stmt.setString(1, receiptID);
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
        rs.close();

        return ans;
    }

    protected static List<Item2Receipt> listItems2Receipt(String receiptID) throws SQLException{
        List<Item2Receipt> ans = new ArrayList<Item2Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento , fc.cantidad , fc.devuelto "
                + "from articulo a , factura_contiene fc where fc.codigo_interno_factura = ? and fc.codigo_de_articulo = a.codigo");
        stmt.setString(1, receiptID);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Item2Receipt(
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
                        ),
                        rs.getInt("cantidad"),
                        rs.getInt("devuelto"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Receipt> listIdleReceiptToday() throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos , identificador_turno "
                + "from factura where estado='Espera' and datediff(fecha_creacion,now()) = 0");
        
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            rs.getDouble("descuento_global"),
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2Receipt(rs.getString("codigo_interno")),
                            rs.getString("identificador_turno")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Receipt> listUncompletedReceiptToday() throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos , identificador_turno "
                + "from factura where estado='Pedido' and datediff(fecha_creacion,now()) = 0");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Receipt r = new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            rs.getDouble("descuento_global"),
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2Receipt(rs.getString("codigo_interno")),
                            rs.getString("identificador_turno")
                        );
            if ( !r.getItems().isEmpty() ){
                ans.add(r);
            }
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static String getMyPrinter() throws SQLException{
        List<PointOfSale> poses = listPointOfSales(true);
        for (PointOfSale pointOfSale : poses) {
            if ( pointOfSale.getId().equals(Constants.myId) ){
                return pointOfSale.getPrinter();
            }
        }
        return null;
    }

    protected static List<Client> listClients(String id) throws SQLException{
        List<Client> ans = new ArrayList<Client>();
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo , nombre, direccion, telefono "
                + "from cliente "
                + "where codigo = ? ");
        stmt.setString(1, id );
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Client(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    public static void createClient(Client client) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(
                "insert into cliente ( codigo, nombre , direccion , telefono ) values ( ? , ? , ? , ? )");
        stmt.setString(1, client.getId());
        stmt.setString(2, client.getName());
        stmt.setString(3, client.getAddress());
        stmt.setString(4, client.getPhone());
        stmt.executeUpdate();
        c.close();
    }

    public static void modifyClient(Client myClient) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update cliente set nombre = ? , direccion = ? , telefono = ? where codigo = ?");
        stmt.setString(1, myClient.getName());
        stmt.setString(2, myClient.getAddress());
        stmt.setString(3, myClient.getPhone());
        stmt.setString(4, myClient.getId());
        stmt.executeUpdate();

        c.close();
    }

    public static boolean wasAssignUsedToday(Assign t) throws SQLException{
        boolean ans = false;

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select * "
                + "from factura where datediff(now(),fecha_creacion) = 0 and identificador_turno = ? and identificador_pos = ?");
        stmt.setString(1, t.getTurn() );
        stmt.setString(2, t.getPos() );
        ResultSet rs = stmt.executeQuery();

        ans = rs.next();

        c.close();
        rs.close();

        return ans;
    }

    public static void deleteAssignToday(Assign t) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from asigna where datediff(now(),fecha) = 0 and identificador_turno = ? and identificador_pos = ?");
        stmt.setString(1, t.getTurn() );
        stmt.setString(2, t.getPos() );
        stmt.executeUpdate();

        c.close();
    }

    public static void setAssignOpen(Assign a, boolean isOpen) throws SQLException, Exception {
        if ( isExpired(a) ){
            throw new Exception("No se puede modificar una asignación expirada");
        }
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update asigna set abierto = ? where identificador_turno = ? and identificador_pos = ? and datediff( fecha, ? ) = 0");
        stmt.setInt(1, isOpen?1:0);
        stmt.setString(2, a.getTurn());
        stmt.setString(3, a.getPos());
        stmt.setDate(4, a.getDate());
        stmt.executeUpdate();

        c.close();
    }

    public static void deleteAllStores() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from almacen");
        stmt.executeUpdate();
        c.close();
    }

    public static void createStore(DefaultTableModel model) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String id = (String) model.getValueAt(i, 0) ;
            String description = (String) model.getValueAt(i, 1);
            PreparedStatement stmt = c.prepareStatement(
                "insert into almacen ( codigo, descripcion ) values ( ? , ? )");
            stmt.setString(1, id);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }
        
        c.close();
    }

    public static List<Store> listStores() throws SQLException{
        List<Store> ans = new ArrayList<Store>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo , descripcion "
                + "from almacen");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Store(
                        rs.getString("codigo"),
                        rs.getString("descripcion")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static void flipEnabledPointOfSale(PointOfSale p) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set habilitada = ? where identificador = ? ");
        stmt.setInt(1, !p.isEnabled()?1:0);
        stmt.setString(2, p.getId());
        stmt.executeUpdate();

        c.close();
    }

    protected static void updateReportZ(String z) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set reporte_z = ? where identificador = ? ");
        stmt.setString(1, z);
        stmt.setString(2, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    static void setFiscalData(String actualId, String serial, String z, String fiscalNumber) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set impresora = ? , numero_fiscal = ? , numero_reporte_z = ? "
                + "where codigo_interno = ? ");
        stmt.setString(1, serial);
        stmt.setString(2, fiscalNumber);
        stmt.setString(3, z);
        stmt.setString(4, actualId);
        stmt.executeUpdate();

        c.close();
    }

    public static void finishReceipt(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Facturada' where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }

    public static void setGlobalDiscount(String receiptId , Double d) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  descuento_global = ? where codigo_interno = ? ");
        stmt.setDouble(1, d);
        stmt.setString(2, receiptId);
        stmt.executeUpdate();

        c.close();
    }

    static void setClient(Client cu, String actualId) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  codigo_de_cliente = ? where codigo_interno = ? ");
        stmt.setString(1, cu.getId());
        stmt.setString(2, actualId);
        stmt.executeUpdate();

        c.close();
    }

    static void setPritingHour(String actualId) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  fecha_impresion = now() where codigo_interno = ? ");
        stmt.setString(1, actualId);
        stmt.executeUpdate();

        c.close();
    }

    public static boolean hasMovements() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select * from factura");
        ResultSet rs = stmt.executeQuery();
        boolean ans = rs.next();

        c.close();
        return ans;
    }

    static void deleteAllBPos() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from punto_de_venta_de_banco");
        stmt.executeUpdate();
        c.close();
    }

    static void createBPOS(DefaultTableModel model) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String id = (String) model.getValueAt(i, 0) ;
            String description = (String) model.getValueAt(i, 1);
            String lot = (String) model.getValueAt(i, 2);
            String posId = (String) model.getValueAt(i, 3);
            String kindbpos = (String) model.getValueAt(i, 4);
            PreparedStatement stmt = c.prepareStatement(
                "insert into punto_de_venta_de_banco ( id, descripcion, lote , identificador_pos, tipo) values ( ? , ? , ? , ? , ? )");
            stmt.setString(1, id);
            stmt.setString(2, description);
            stmt.setString(3, lot);
            stmt.setString(4, posId);
            stmt.setString(5, kindbpos);
            stmt.executeUpdate();
        }

        c.close();
    }

    static List<BankPOS> listBPos() throws SQLException {
        List<BankPOS> ans = new ArrayList<BankPOS>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion, lote, identificador_pos , tipo from punto_de_venta_de_banco");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new BankPOS(
                        rs.getString("id"),
                        rs.getString("descripcion"),
                        rs.getString("lote"),
                        rs.getString("identificador_pos"),
                        rs.getString("tipo")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    static List<BankPOS> listBPos(String pos, String kindbpos) throws SQLException {
        List<BankPOS> ans = new ArrayList<BankPOS>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion, lote, identificador_pos , tipo from punto_de_venta_de_banco "
                + "where identificador_pos = ? and ( tipo = ?  or tipo = ? ) ");
        stmt.setString(1, Constants.myId);
        stmt.setString(2, kindbpos);
        stmt.setString(3, Constants.kindOfBPOS[Constants.kindOfBPOS.length-1]);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new BankPOS(
                        rs.getString("id"),
                        rs.getString("descripcion"),
                        rs.getString("lote"),
                        rs.getString("identificador_pos"),
                        rs.getString("tipo")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    public static void savePayForm(List<PayForm> lpf) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        for (PayForm payForm : lpf) {
            PreparedStatement stmt = c.prepareStatement(
                "insert into forma_de_pago ( fecha, codigo_interno_factura, tipo, codigo_punto_de_venta_de_banco , lote , monto, codigo_punto_de_venta) "
                + "values ( now(), ? , ? , ? , ? , ? , ?)");
            stmt.setString(1, payForm.getReceiptId());
            stmt.setString(2, payForm.getFormWay());
            stmt.setString(3, payForm.getbPos());
            stmt.setString(4, payForm.getLot());
            stmt.setDouble(5, payForm.getQuant());
            stmt.setString(6, Constants.myId);
            stmt.executeUpdate();
            if ( payForm.getFormWay().equals("Efectivo") ){
                addCash(payForm.getQuant(), Constants.myId);
            }else if ( payForm.getFormWay().equals("Debito") ){
                addDebit(payForm.getQuant(), Constants.myId);
            }else if ( payForm.getFormWay().equals("Credito") ){
                addCredit(payForm.getQuant(), Constants.myId);
            }else if ( payForm.getFormWay().equals("Cambio") ){
                addCash(-1*payForm.getQuant(), Constants.myId);
            }else if ( payForm.getFormWay().equals("Nota de Credito") ){
                addCreditNote(payForm.getQuant(), Constants.myId);
            } else {
                // This should not happend
                assert(false);
            }
        }

        c.close();
    }

    public static void createCreditNote(String myId, String idReceipt, String user, Assign assign, List<Item2Receipt> items) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        double subT = .0 , ivaT = .0 , total = .0 ;
        for (Item2Receipt item2r : items) {
            Item item = item2r.getItem();
            subT += Shared.round( item.getLastPrice().withDiscount(item.getDescuento()).getQuant(), 2 );
        }
        ivaT = new Price(null, subT).getIva().getQuant();
        total = subT + ivaT;

        PreparedStatement stmt = c.prepareStatement("insert into nota_de_credito"
                + " ( codigo_interno, codigo_factura, estado, fecha_creacion , total_sin_iva , total_con_iva , iva, codigo_de_usuario, cantidad_de_articulos , identificador_turno , identificador_pos) "
                + "values ( ?, ? , 'Pedido' , now() , ? , ?, ? , ? , ? , ? , ?)");
        stmt.setString(1, myId);
        stmt.setString(2, idReceipt);
        stmt.setDouble(3, subT);
        stmt.setDouble(4, total);
        stmt.setDouble(5, ivaT);
        stmt.setString(6, user);
        stmt.setInt(7, items.size());
        stmt.setString(8, assign.getTurn());
        stmt.setString(9, assign.getPos());
        stmt.executeUpdate();

        c.close();

        for (Item2Receipt item2r : items) {
            addItem2CreditNote(myId, item2r);
            deleteItemFromReceipt(item2r,idReceipt);
        }
    }

    protected static int lastCreditNoteToday() throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select count(*) from nota_de_credito "
                + "where datediff(now(),fecha_creacion) = 0");

        boolean ok = rs.next();
        assert(ok);
        int ans = rs.getInt(1);
        c.close();
        rs.close();

        return ans;
    }

    public static Receipt getReceiptToDev(String id) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno , estado , fecha_creacion , codigo_de_cliente , total_con_iva ,"
                + "impresora , numero_fiscal , numero_reporte_z , descuento_global"
                + " from factura where codigo_interno = ? and estado='Facturada'");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();

        Receipt ans = null;
        if ( ok ){
            ans = new Receipt(id, "Facturada",rs.getTimestamp("fecha_creacion"), null, rs.getString("codigo_de_cliente")
                    , null, rs.getDouble("total_con_iva"), rs.getDouble("descuento_global"), null, rs.getString("impresora"),
                    rs.getString("numero_fiscal"), rs.getString("numero_reporte_z"),
                    null, null, listItems2Receipt(id), null);
        }
        c.close();
        rs.close();
        return ans;
    }

    protected static void addItem2CreditNote(String receiptId, Item2Receipt item) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into nota_de_credito_contiene"
                + " ( codigo_interno_nota_de_credito, codigo_de_articulo , cantidad, devuelto) "
                + "values ( ? , ? , ? , 0 )");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getItem().getCode());
        stmt.setInt(3, item.getQuant());
        stmt.executeUpdate();

        changeItemStock(item.getItem().getCode(), 1);

        double withoutTax = item.getItem().getLastPrice().getQuant();
        double subT = .0;// = accumulatedInReceipt(receiptId) + withoutTax;
        stmt = c.prepareStatement("update nota_de_credito "
                + "set total_sin_iva = " + (subT) +
                " , total_con_iva =" + (new Price(null,subT)).plusIva().getQuant() +
                " , iva = " + (new Price(null,subT)).getIva().getQuant() +
                " , cantidad_de_articulos = cantidad_de_articulos + 1 "
                + "where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();

    }

    public static void changeLot( String idBpos , String newLot) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update punto_de_venta_de_banco "
                + "set lote = ? where id = ? ");
                
        stmt.setString(1, newLot);
        stmt.setString(2, idBpos);
        stmt.executeUpdate();

        c.close();
    }

    public static Double getCashToday(String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select dinero_efectivo "
                + "from dia_operativo where datediff(now(),fecha) = 0 and codigo_punto_de_venta = ? ");
        stmt.setString(1, Constants.myId );
        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();

        if ( !ans ){
            c.close();
            rs.close();
            return -1.0;
        }

        Double doubl = rs.getDouble("dinero_efectivo");

        c.close();
        rs.close();

        return doubl;
    }

    public static void setCash(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_efectivo = ? where codigo_punto_de_venta = ? ");

        stmt.setDouble(1, money);
        stmt.setString(2, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    public static void addCash(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_efectivo = dinero_efectivo + ? where codigo_punto_de_venta = ? ");

        stmt.setDouble(1, money);
        stmt.setString(2, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    public static void addCredit(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_tarjeta_credito = dinero_tarjeta_credito + ? where codigo_punto_de_venta = ? ");

        stmt.setDouble(1, money);
        stmt.setString(2, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    public static void addCreditNote(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set nota_de_credito = nota_de_credito + ? where codigo_punto_de_venta = ? ");

        stmt.setDouble(1, money);
        stmt.setString(2, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    public static void addDebit(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_tarjeta_debito = dinero_tarjeta_debito + ? where codigo_punto_de_venta = ? ");

        stmt.setDouble(1, money);
        stmt.setString(2, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    public static void newCash(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("insert into dia_operativo ( fecha , codigo_punto_de_venta , dinero_tarjeta_credito "
                + ", dinero_efectivo , dinero_tarjeta_debito , nota_de_credito ) values ( curdate() , ? , .0 , ? , .0 , 0)");

        stmt.setDouble(2, money);
        stmt.setString(1, Constants.myId);
        stmt.executeUpdate();

        c.close();
    }

    public static Time getDiff(Time t) throws SQLException, ParseException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select convert(timediff( curtime(), ? ),char)");
        stmt.setTime(1, t);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String ansS = rs.getString(1);
        ansS = ansS.substring(1);
        Time ans = new Time(sdf.parse(ansS).getTime());
        c.close();
        rs.close();
        return ans;
    }

    private static void deleteItemFromReceipt(Item2Receipt item2r, String receiptId) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura_contiene set devuelto = devuelto + ? "
                + "where codigo_interno_factura = ? and cantidad > devuelto and  codigo_de_articulo = ? limit 1");
        stmt.setString(2, receiptId);
        stmt.setInt(1, item2r.getQuant());
        stmt.setString(3, item2r.getItem().getCode());
        stmt.executeUpdate();
    }

    public static List<Expense> listExpensesToday() throws SQLException {
        List<Expense> ans = new ArrayList<Expense>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select concepto , monto from gasto "
                + "where datediff(now(),fecha) = 0 ");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Expense(
                        rs.getString("concepto"),
                        rs.getDouble("monto")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    public static void deleteAllExpensesToday() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from gasto where datediff(now(),fecha) = 0 ");
        stmt.executeUpdate();
        c.close();
    }

    public static void createExpensesToday(DefaultTableModel model) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String concept = (String) model.getValueAt(i, 0) ;
            Double quant = Double.parseDouble(((String) model.getValueAt(i, 1)).replace(',', '.'));
            PreparedStatement stmt = c.prepareStatement(
                "insert into gasto ( fecha, concepto, monto) values ( now() , ? , ? )");
            stmt.setString(1, concept);
            stmt.setDouble(2, quant);
            stmt.executeUpdate();
        }
        c.close();
    }

    static JRDataSource createDataSource(List<Parameter> parameters, String sql, List<Column> columns) throws SQLException {
        String[] columnsArray = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            Column c = columns.get(i);
            columnsArray[i] = c.getName();
        }
        DataSource dataSource = new DataSource(columnsArray);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        for (int i = 0; i < parameters.size(); i++) {
            Parameter p = parameters.get(i);
            stmt.setString(i+1, p.getTextField().getText());
        }
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] toAdd = new Object[columns.size()];
            for (int i = 0; i < columnsArray.length; i++) {
                if ( columns.get(i).getMyClass().equals("bigDecimalType") ){
                    toAdd[i] = new BigDecimal(rs.getDouble(columnsArray[i]));
                }else{
                    toAdd[i] = rs.getString(columnsArray[i]);
                }
                
            }
            dataSource.add(toAdd);
        }

        c.close();
        rs.close();

        return dataSource;
    }

    public static List<Deposit> listDepositsToday() throws SQLException {
        List<Deposit> ans = new ArrayList<Deposit>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select banco, planilla, cataporte, monto from deposito "
                + "where datediff(now(),fecha) = 0 ");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Deposit(
                        rs.getString("banco"),
                        rs.getString("planilla"),
                        rs.getString("cataporte"),
                        rs.getDouble("monto")
                        )
                    );
        }
        
        c.close();
        rs.close();
        
        return ans;
    }

    public static void deleteAllDepositsToday() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from deposito where datediff(now(),fecha) = 0 ");
        stmt.executeUpdate();
        c.close();
    }

    public static void createDepositsToday(DefaultTableModel model) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String bank = (String) model.getValueAt(i, 0) ;
            Double quant = Double.parseDouble(((String) model.getValueAt(i, 3)).replace(',', '.'));
            String formId = (String) model.getValueAt(i, 1) ;
            String cataport = (String) model.getValueAt(i, 2) ;
            PreparedStatement stmt = c.prepareStatement(
                "insert into deposito ( fecha, banco, planilla, cataporte, monto ) values ( now() , ? , ? , ? , ? )");
            stmt.setString(1, bank);
            stmt.setString(2, formId);
            stmt.setString(3, cataport);
            stmt.setDouble(4, quant);
            stmt.executeUpdate();
        }
        c.close();
    }

    public static void listFormWayXPosToday(DefaultTableModel ans) throws SQLException{
        ans.setRowCount(0);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_punto_de_venta "
                + ", dinero_efectivo, dinero_tarjeta_credito + dinero_tarjeta_debito as dinero_tarjeta , "
                + "nota_de_credito from dia_operativo where datediff(fecha,now()) = 0");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            String[] s = {rs.getString("codigo_punto_de_venta"),rs.getString("dinero_efectivo")
                    ,rs.getString("dinero_tarjeta"),rs.getString("nota_de_credito")};
            ans.addRow(s);
        }

        c.close();
        rs.close();

    }

    public static void listFiscalZ(DefaultTableModel ans) throws SQLException{
        ans.setRowCount(0);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select do.codigo_punto_de_venta , pos.impresora , "
                + "do.dinero_tarjeta_credito + do.dinero_efectivo+ do.dinero_tarjeta_debito+ do.nota_de_credito as facturado, "
                + "dinero_efectivo_impresora+dinero_tarjeta_credito_impresora+dinero_tarjeta_debito_impresora+nota_de_credito_impresora as facturado_impresora "
                + "from dia_operativo as do , punto_de_venta as pos where datediff(fecha,now())=0 and do.codigo_punto_de_venta "
                + "= pos.identificador");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            String[] s = {rs.getString("codigo_punto_de_venta"),rs.getString("impresora")
                    ,rs.getString("facturado"),rs.getString("facturado_impresora")};
            ans.addRow(s);
        }

        c.close();
        rs.close();
    }

    static void listFormWayXPosesDetailToday(DefaultTableModel ans) throws SQLException {
        ans.setRowCount(0);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_punto_de_venta,tipo,sum(monto) "
                + "as monto from forma_de_pago where datediff(curdate(),fecha)=0 "
                + "group by codigo_punto_de_venta , tipo");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            String[] s = {rs.getString("codigo_punto_de_venta"),rs.getString("tipo"),rs.getString("monto")};
            ans.addRow(s);
        }

        c.close();
        rs.close();
    }

    static void listBankTable(DefaultTableModel ans) throws SQLException {
        ans.setRowCount(0);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select b.descripcion , a.lote,"
                + "a.tipo , sum(monto) as monto from forma_de_pago a,"
                + "punto_de_venta_de_banco b where a.codigo_punto_de_venta_de_banco = b.id and "
                + "datediff(curdate(),fecha)=0 group by a.codigo_punto_de_venta_de_banco");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            String[] s = {rs.getString("descripcion"),rs.getString("lote"),rs.getString("tipo"),rs.getString("monto")};
            ans.addRow(s);
        }

        c.close();
        rs.close();
    }

    static Double getTotalCardsToday() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto "
                + "from forma_de_pago a, punto_de_venta_de_banco b "
                + "where a.codigo_punto_de_venta_de_banco = b.id and datediff(curdate(),fecha)=0");
        ResultSet rs = stmt.executeQuery();
        
        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble("monto");

        c.close();
        rs.close();
        return ans;
    }

    static Double getTotalCashToday() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto "
                + "from forma_de_pago where datediff(curdate(),fecha)=0 and tipo='Efectivo'");
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble("monto");

        c.close();
        rs.close();
        return ans;
    }

    static Double getExpensesToday() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select monto from gasto where datediff(curdate(),fecha)=0");
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        Double ans = .0;
        if ( ok ){
            ans = rs.getDouble("monto");
        }
        
        c.close();
        rs.close();
        return ans;
    }

    static void markToUpdateFiscalNumbersToday() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update dia_operativo set actualizar_valores=1 "
                + "where reporteZ = 0 and datediff(curdate(),fecha)=0");
        stmt.executeUpdate();
    }

    static boolean isNeededtoUpdate() throws SQLException {
        boolean ans ;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select actualizar_valores from dia_operativo where datediff(curdate(),fecha)=0 and codigo_punto_de_venta= ? ");
        stmt.setString(1, Constants.myId);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = (rs.getInt("actualizar_valores") == 1);
        }else{
            // I am JUST opening the pos.
            ans = false;
        }

        c.close();
        rs.close();
        return ans;
    }

    static void updateFiscalNumbers(Double cash, Double cn, Double debit, Double credit) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update dia_operativo set actualizar_valores = 0 , dinero_efectivo_impresora = ? , "
                + "dinero_tarjeta_credito_impresora = ? , dinero_tarjeta_debito_impresora = ? , nota_de_credito_impresora = ? where "
                + "datediff(curdate(),fecha)=0 and codigo_punto_de_venta= ? ");
        stmt.setDouble(1, cash);
        stmt.setDouble(2, credit);
        stmt.setDouble(3, cn);
        stmt.setDouble(4, debit);
        stmt.setString(5, Constants.myId);
        stmt.executeUpdate();
    }

    // WARNING!! NON-ESCAPED STRING
    static void mirrorTable(String tableName) throws SQLException, PropertyVetoException{

        if ( !Constants.isPos ){
            // Admin has no mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Constants.mirrorDbHost + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection a = ConnectionDrivers.cpds.getConnection(); // This is updated
        Connection b = ConnectionDrivers.cpdsMirror.getConnection(); // This is outdated.

        PreparedStatement stmtB = b.prepareStatement("delete from " + tableName); // Good bye old data.

        stmtB.executeUpdate();

        PreparedStatement stmtA = a.prepareStatement("select * from "+ tableName); // Getting the new data.
        ResultSet rsA = stmtA.executeQuery();
        ResultSetMetaData rsMetaDataA = rsA.getMetaData();

        while( rsA.next() ){
            String sql = "insert into " + tableName + " values ( ";
            for (int i = 0; i < rsMetaDataA.getColumnCount()-1; i++) {
                sql += "?,";
            }
            sql += "?)";

            PreparedStatement stmtNewB = b.prepareStatement(sql);

            for (int i = 0; i < rsMetaDataA.getColumnCount(); i++) {
                stmtNewB.setString(i+1, rsA.getString(i+1));
            }

            stmtNewB.executeUpdate();
        }

        rsA.close();
        a.close();
        b.close();

    }

    static void updateStock() throws PropertyVetoException, SQLException{
        if ( !Constants.isPos ){
            // Admin has no mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Constants.mirrorDbHost + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection b = ConnectionDrivers.cpds.getConnection();
        Connection a = ConnectionDrivers.cpdsMirror.getConnection();


        PreparedStatement stmtA = a.prepareStatement("select codigo_de_articulo,cantidad-devuelto as cantidad "
                + "from factura_contiene where cantidad-devuelto != 0");
        ResultSet rsA = stmtA.executeQuery();

        while( rsA.next() ){
            
            PreparedStatement stmtNewB = b.prepareStatement("update articulo set existencia_actual = existencia_actual - ? where codigo = ?");

            stmtNewB.setInt(1, rsA.getInt("cantidad"));
            stmtNewB.setString(2, rsA.getString("codigo_de_articulo"));

            stmtNewB.executeUpdate();
        }
        
        rsA.close();
        a.close();
        b.close();
    }

    static void cleanMirror(String tableName) throws PropertyVetoException, SQLException{
        if ( !Constants.isPos ){
            // Admin has no mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Constants.mirrorDbHost + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection b = ConnectionDrivers.cpds.getConnection();
        Connection a = ConnectionDrivers.cpdsMirror.getConnection();


        PreparedStatement stmtA = a.prepareStatement("select * from "+ tableName);
        ResultSet rsA = stmtA.executeQuery();
        ResultSetMetaData rsMetaDataA = rsA.getMetaData();

        while( rsA.next() ){
            String sql = "insert into " + tableName + " values (";
            for (int i = 0; i < rsMetaDataA.getColumnCount()-1; i++) {
                sql += "?,";
            }
            sql += "?)";

            PreparedStatement stmtNewB = b.prepareStatement(sql);

            for (int i = 0; i < rsMetaDataA.getColumnCount(); i++) {
                stmtNewB.setString(i+1, rsA.getString(i+1));
            }

            stmtNewB.executeUpdate();
        }


        PreparedStatement stmtA2 = a.prepareStatement("delete from " + tableName);

        stmtA2.executeUpdate();

        rsA.close();
        a.close();
        b.close();
    }

}
