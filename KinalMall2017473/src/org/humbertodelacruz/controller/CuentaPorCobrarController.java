
package org.humbertodelacruz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Administracion;
import org.humbertodelacruz.bean.Clientes;
import org.humbertodelacruz.bean.CuentaPorCobrar;
import org.humbertodelacruz.bean.Locales;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;


public class CuentaPorCobrarController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <CuentaPorCobrar> listaCuentaPorCobrar;
    private  ObservableList <Administracion> listaAdministracion;
    private ObservableList <Clientes> listaClientes;
    private  ObservableList <Locales> listaLocales;
    
   
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private  ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblCuentaPorCobrar;
    @FXML private TableColumn colCodigoCuentaPorCobrar;
    @FXML private TableColumn colNumeroFactura;
    @FXML private TableColumn colAnio;
    @FXML private TableColumn colMes;
    @FXML private TableColumn colValorNetoPago;
    @FXML private TableColumn colEstadoPago;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colCodigoLocal;
    @FXML private TextField txtCodigoCuentaPorCobrar;
    @FXML private TextField txtNumerofactura;
    @FXML private TextField txtAnio;
    @FXML private TextField txtMes;
    @FXML private TextField txtValorNetoPago;
    @FXML private TextField txtEstadoPago;
    @FXML private ComboBox cmbCodigoAdministracion;
    @FXML private ComboBox cmbCodigoCliente;
    @FXML private ComboBox cmbCodigoLocal;
        

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblCuentaPorCobrar.setItems(getCuentaPorCobrar());
        colCodigoCuentaPorCobrar.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Integer> ("codigoCuentasPorCobrar"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, String> ("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Integer> ("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Integer> ("mes"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Double> ("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, String> ("estadoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer> ("codigoAdministracion"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer> ("codigoCliente"));
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer> ("codigoLocal"));
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoCliente.setItems(getClientes());
        cmbCodigoLocal.setItems(getLocales());
            
    }
    
    public ObservableList<CuentaPorCobrar> getCuentaPorCobrar(){
        ArrayList<CuentaPorCobrar> lista = new ArrayList<CuentaPorCobrar>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorCobrar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CuentaPorCobrar(resultado.getInt("codigoCuentasPorCobrar"), 
                                                resultado.getString("numeroFactura"), 
                                                resultado.getInt("anio"), 
                                                resultado.getInt("mes"), 
                                                resultado.getDouble("valorNetoPago"), 
                                                resultado.getString("estadoPago"), 
                                                resultado.getInt("codigoAdministracion"), 
                                                resultado.getInt("codigoCliente"), 
                                                resultado.getInt("codigoLocal")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCuentaPorCobrar = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList <Administracion> getAdministracion(){
            ArrayList<Administracion> lista=new ArrayList<Administracion>();
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}"); //prepareCall hcae la llamda del elemnto de Mysql (procedimiento almacenado)
                ResultSet resultado = procedimiento.executeQuery();
                //resultado.next = true - ese es su valor por defecto
                while (resultado.next()) {
                    lista.add(new Administracion(resultado.getInt("codigoAdministracion"),
                                                    resultado.getString("direccion"),
                                                    resultado.getString("telefono")));
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Aquí se realizó el casteo, lo que estaba alamcenadao en ArrayList ahora esta en el ObservableList
            return listaAdministracion = FXCollections.observableArrayList(lista);
        }
    
    
    public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes> lista = new ArrayList<Clientes>();
        try {
            //LLAMA AL PROCEDIMIENTO ALMACENADO
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            //SE EJECUTA Y GUARDA EN UNA VARIABLE EL PROCEDIMIENTO ALMACENADO
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                //AL ARRAYLIST SE LE AGREGAN Y ORDENAN LOS DATOS OBTENIDOS(GET) DEL PROCDIMEINTO ALMACENAOD
                lista.add(new Clientes(resultado.getInt("codigoCliente"), 
                                        resultado.getString("nombresCliente"), 
                                        resultado.getString("apellidosCliente"), 
                                        resultado.getString("telefonoCliente"), 
                                        resultado.getString("direccionCliente"), 
                                        resultado.getString("email"), 
                                        resultado.getInt("codigoLocal"), 
                                        resultado.getInt("codigoAdministracion"), 
                                        resultado.getInt("codigoTipoCliente")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList<Locales> getLocales(){
        ArrayList<Locales> lista = new ArrayList<Locales>();
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
            ResultSet resultado = Procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Locales(resultado.getInt("codigoLocal"), 
                        resultado.getDouble("saldoFavor"), 
                        resultado.getDouble("saldoContra"), 
                        resultado.getInt("mesesPendientes"), resultado.getBoolean("disponibilidad"), 
                        resultado.getDouble("valorLocal"), 
                        resultado.getDouble("valorAdministracion")));
            }

        } catch (Exception e) {
            e.printStackTrace();;
        }
        return  listaLocales = FXCollections.observableArrayList(lista);
    }

    
    
    
    
/* *****************************************************************************/
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        this.escenarioPrincipal.menuPrincipal();
    }
    
    
/* *****************************************************************************/    

    public void activarControles(){
        txtCodigoCuentaPorCobrar.setEditable(false);
        txtNumerofactura.setEditable(true);
        txtAnio.setEditable(true);
        txtMes.setEditable(true);
        txtValorNetoPago.setEditable(true);
        txtEstadoPago.setEditable(true);
        cmbCodigoAdministracion.setDisable(false);
        cmbCodigoCliente.setDisable(false);
        cmbCodigoLocal.setDisable(false);
        
    }
    
        public void activarControles2(){
        txtCodigoCuentaPorCobrar.setEditable(false);
        txtNumerofactura.setEditable(true);
        txtAnio.setEditable(true);
        txtMes.setEditable(true);
        txtValorNetoPago.setEditable(true);
        txtEstadoPago.setEditable(true);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoCliente.setDisable(true);
        cmbCodigoLocal.setDisable(true);
        
    }
    
    
    public void desactivarControles(){
        txtCodigoCuentaPorCobrar.setEditable(false);
        txtNumerofactura.setEditable(false);
        txtAnio.setEditable(false);
        txtMes.setEditable(false);
        txtValorNetoPago.setEditable(false);
        txtEstadoPago.setEditable(false);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoCliente.setDisable(true);
        cmbCodigoLocal.setDisable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoCuentaPorCobrar.clear();
        txtNumerofactura.clear();
        txtAnio.clear();
        txtMes.clear();
        txtValorNetoPago.clear();
        txtEstadoPago.clear();
        cmbCodigoAdministracion.setValue(null);
        cmbCodigoCliente.setValue(null);
        cmbCodigoLocal.setValue(null);
        
    }
/* *****************************************************************************/  

        public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        CuentaPorCobrar registro = new CuentaPorCobrar();
        registro.setNumeroFactura(txtNumerofactura.getText());
        registro.setAnio(Integer.parseInt(txtAnio.getText()));
        registro.setMes(Integer.parseInt(txtMes.getText()));
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoCliente(((Clientes)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        registro.setCodigoLocal(((Locales)cmbCodigoLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentaPorCobrar(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNumeroFactura());
            procedimiento.setInt(2, registro.getAnio());
            procedimiento.setInt(3, registro.getMes());
            procedimiento.setDouble(4, registro.getValorNetoPago());
            procedimiento.setString(5, registro.getEstadoPago());
            procedimiento.setInt(6, registro.getCodigoAdministracion());
            procedimiento.setInt(7, registro.getCodigoCliente());
            procedimiento.setInt(8, registro.getCodigoLocal());
            
            procedimiento.execute();
            cargarDatos();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
/* *****************************************************************************/

        public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
            default:
                if(tblCuentaPorCobrar.getSelectionModel().getSelectedItem() != null){
                    int resultado = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Elemento?", "Elminar Cuenta Por Cobrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentaPorCobrar(?)}");
                            procedimiento.setInt(1, ((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorCobrar());
                            
                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
            break;
                
    
        }        
    }
 
/* *****************************************************************************/
            
    public void seleccionarElemento(){
        if(tblCuentaPorCobrar.getSelectionModel().getSelectedItem() != null){
            txtCodigoCuentaPorCobrar.setText(String.valueOf(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorCobrar()));
            txtNumerofactura.setText(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            txtAnio.setText(String.valueOf(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getAnio()));
            txtMes.setText(String.valueOf(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getMes()));
            txtValorNetoPago.setText(String.valueOf(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            txtEstadoPago.setText(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());
            cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCodigoCliente.getSelectionModel().select(buscarCliente(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            cmbCodigoLocal.getSelectionModel().select(buscarLocal(((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public Administracion buscarAdministracion(int codigoAdministracion){
        Administracion resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, codigoAdministracion);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Administracion(registro.getInt("codigoAdministracion"),
                                                registro.getString("direccion"), 
                                                registro.getString("telefono"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado; 
    }
    
    
    public Locales buscarLocal(int codigoLocal){
        //En esta variable se almacenará el registro completo
        Locales resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarLocal(?)}");
            procedimiento.setInt(1, codigoLocal);
            ResultSet registro = procedimiento.executeQuery(); 
            while(registro.next()){
                resultado = new Locales(registro.getInt("codigoLocal"), 
                                        registro.getDouble("saldoFavor"), 
                                        registro.getDouble("saldoContra"), 
                                        registro.getInt("mesesPendientes"), 
                                        registro.getBoolean("disponibilidad"), 
                                        registro.getDouble("valorLocal"), 
                                        registro.getDouble("valorAdministracion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return resultado;
    }
    
    
    public Clientes buscarCliente (int codigoCliente){
        Clientes resultado = null;
        try {
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCliente(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Clientes(registro.getInt("codigoCliente"), 
                                        registro.getString("nombresCliente"), 
                                        registro.getString("apellidosCliente"), 
                                        registro.getString("telefonoCliente"), 
                                        registro.getString("direccionCliente"), 
                                        registro.getString("email"), 
                                        registro.getInt("codigoLocal"), 
                                        registro.getInt("codigoAdministracion"), 
                                        registro.getInt("codigoTipoCliente"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    

/* *****************************************************************************/
    
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCuentaPorCobrar.getSelectionModel().getSelectedItem()!= null){
                    activarControles2();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar el elemento");
                }
            break;
            
            
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
            
        }
    }
    
    
    public void actualizar(){
        CuentaPorCobrar registro = ((CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem());
        
        registro.setNumeroFactura(txtNumerofactura.getText());
        registro.setAnio(Integer.parseInt(txtAnio.getText()));
        registro.setMes(Integer.parseInt(txtMes.getText()));
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        registro.setEstadoPago(txtEstadoPago.getText());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentaPorCobrar(?,?,?,?,?,?)}");
            
            procedimiento.setInt(1, registro.getCodigoCuentasPorCobrar());
            procedimiento.setString(2, registro.getNumeroFactura());
            procedimiento.setInt(3, registro.getAnio());
            procedimiento.setInt(4, registro.getMes());
            procedimiento.setDouble(5, registro.getValorNetoPago());
            procedimiento.setString(6, registro.getEstadoPago());
            
            procedimiento.execute();
            cargarDatos();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /* *****************************************************************************/    
        public void reporte(){
           switch(tipoDeOperaciones){
               case ACTUALIZAR:
                   desactivarControles();
                   limpiarControles();
                   btnEditar.setText("Editar");
                   btnReporte.setText("Reporte");
                   imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                   imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                   btnNuevo.setDisable(false);
                   btnEliminar.setDisable(false);
                   tipoDeOperaciones = operaciones.NINGUNO;
                   break;
                   
           }
        }
    
}
