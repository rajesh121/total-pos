package totalpos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Saúl Hidalgo
 */
public class ConnectionDrivers {

    protected static ComboPooledDataSource cpds ;    

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

    protected static boolean isAllowed(String profile, String id) throws SQLException, Exception{

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

    protected static void initializeConfig(){
        try {
            Shared.getConfig().clear();
            Connection c = ConnectionDrivers.cpds.getConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select `Key` , `Value` from configuracion");
            while (rs.next()) {
                Shared.getConfig().put(rs.getString("Key"), rs.getString("Value"));
            }
            c.close();
            rs.close();
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "Problemas con la base de datos.", ex);
            msb.show(Shared.getMyMainWindows());
        }
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
        ResultSet rs = stmt.executeQuery("select identificador, descripcion, impresora from punto_de_venta");

        List<PointOfSale> ans = new ArrayList<PointOfSale>();
        while ( rs.next() ) {
            ans.add( new PointOfSale(
                    rs.getString("identificador"),
                    rs.getString("descripcion"),
                    rs.getString("impresora")) );
        }
        c.close();
        rs.close();

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

    protected static void createReceipt(String id, String user) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into factura"
                + " ( codigo_interno, estado, fecha_creacion , total_sin_iva , total_con_iva , iva, codigo_de_usuario, cantidad_de_articulos ) "
                + "values ( ? , 'Pedido' , now() , 0, 0, 0 , ? , 0 )");
        stmt.setString(1, id);
        stmt.setString(2, user);
        stmt.executeUpdate();

        c.close();
    }

    protected static void addItem2Receipt(String receiptId, Item item) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into factura_contiene"
                + " ( codigo_interno_factura, codigo_de_articulo ) "
                + "values ( ? , ? )");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getCode());
        stmt.executeUpdate();

        changeItemStock(item.getCode(), -1);

        double withoutTax = item.getLastPrice().getQuant();
        double withTax = item.getLastPrice().plusIva().getQuant();
        stmt = c.prepareStatement("update factura "
                + "set total_sin_iva = total_sin_iva + " + withoutTax +
                " , total_con_iva = total_con_iva + " + withTax +
                " , iva = iva + " + (withTax-withoutTax) +
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
        double withTax = -1*item.getLastPrice().plusIva().getQuant();
        stmt = c.prepareStatement("update factura "
                + "set total_sin_iva = total_sin_iva + " + withoutTax +
                " , total_con_iva = total_con_iva + " + withTax +
                " , iva = iva + " + (withTax-withoutTax) +
                " , cantidad_de_articulos = cantidad_de_articulos + 1 "
                + "where codigo_interno = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

    }

    protected static int lastReceiptToday() throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select count(*) from factura "
                + "where datediff(now(),fecha_creacion) = 0");

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
                + " ( identificador, descripcion, impresora ) "
                + "values ( ? , ? , ? )");
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
                + "identificador_pos , fecha , abierto , dinero_efectivo , dinero_tarjeta_credito ,"
                + " dinero_tarjeta_debito from asigna where datediff(fecha,now()) = 0");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new Assign(
                        rs.getString("identificador_turno"),
                        rs.getString("identificador_pos"),
                        rs.getDate("fecha"),
                        rs.getBoolean("abierto"),
                        rs.getDouble("dinero_efectivo"),
                        rs.getDouble("dinero_tarjeta_credito"),
                        rs.getDouble("dinero_tarjeta_debito"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<PointOfSale> listPointOfSales() throws SQLException{
        List<PointOfSale> ans = new ArrayList<PointOfSale>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador , descripcion , impresora "
                + "from punto_de_venta");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new PointOfSale(
                        rs.getString("identificador"),
                        rs.getString("descripcion"),
                        rs.getString("impresora"))
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
                + "abierto , dinero_efectivo , dinero_tarjeta_credito, "
                + "dinero_tarjeta_debito )  values ( ? , ? , now() , ? , ? , ? , ? )");
        stmt.setString(1, a.getTurn());
        stmt.setString(2, a.getPos());
        stmt.setBoolean(3, a.isOpen());
        stmt.setDouble(4, a.getCash());
        stmt.setDouble(5, a.getCreditCard());
        stmt.setDouble(6, a.getDebitCard());
        stmt.executeUpdate();

        c.close();
    }

    private static boolean assignIsOk(Assign a) throws SQLException{
        List<Assign> l = listAssignsTurnPosToday();
        Turn newTurn = Shared.getTurn(listTurns(), a.getTurn());
        for (Assign assign : l) {
            Turn t = Shared.getTurn(listTurns(), assign.getTurn());
            // Yo denominaría a esto como un if en Cascada :-o!
            if ( a.getPos().equals(assign.getPos()) &&
                    (a.getTurn().equals(assign.getTurn()) ||
                        ( (t.getInicio().before(newTurn.getFin()) && newTurn.getInicio().before(t.getFin())) ||
                            (newTurn.getInicio().before(t.getFin()) && t.getInicio().before(newTurn.getFin())) ) )){
                return false;
            }
        }

        return true;
    }

    protected static List<Assign> listAssignsTurnPosRightNow() throws SQLException{
        List<Assign> ans = new ArrayList<Assign>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.identificador_turno, "
                + "a.identificador_pos , a.fecha , a.abierto , a.dinero_efectivo , a.dinero_tarjeta_credito ,"
                + " a.dinero_tarjeta_debito "
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
                        rs.getBoolean("abierto"),
                        rs.getDouble("dinero_efectivo"),
                        rs.getDouble("dinero_tarjeta_credito"),
                        rs.getDouble("dinero_tarjeta_debito"))
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

    protected static List<Receipt> listIdleReceiptToday() throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos "
                + "from factura where estado='Espera' and datediff(fecha_creacion,now()) = 0");
        
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getDate("fecha_creacion"),
                            rs.getDate("fecha_impresion"),
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
                            listItems(rs.getString("codigo_interno"))
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static String getMyPrinter() throws SQLException{
        List<PointOfSale> poses = listPointOfSales();
        for (PointOfSale pointOfSale : poses) {
            if ( pointOfSale.getId().equals(Constants.myId) ){
                return pointOfSale.getPrinter();
            }
        }
        return null;
    }

}
