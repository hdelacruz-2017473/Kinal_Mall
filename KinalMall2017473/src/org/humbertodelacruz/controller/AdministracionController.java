package org.humbertodelacruz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Administracion;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.report.GenerarReporte;
import org.humbertodelacruz.system.Principal;


public class AdministracionController implements Initializable {

    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Administracion> listaAdministracion; //ALMACENARÁ LOS DATOS DEL MODELO "ADMINISTRACIÓN"
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TextField txtCodigoAdministracion;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblAdministracion;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    private Principal escenarioPrincipal;


    @Override //El override sobreescribe este metodo, el metodo Initialize
    public void initialize(URL location, ResourceBundle resources) {
            cargarDatos();       
    }
        
        public void cargarDatos(){
            tblAdministracion.setItems(getAdministracion()); //LA TABLA YA CONOCE LOS DATOS PERO LOS MUESTRA POR MEDIO DE LAS COLUMNAS SIGUIENTES:
            colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
            colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String> ("direccion"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String> ("telefono"));
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
        
        public void nuevo(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    limpiarControles();
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage( new Image ("/org/humbertodelacruz/images/guardar.png"));
                    imgEliminar.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperacion = operaciones.GUARDAR; 
                    break;
                    
                case GUARDAR:
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage( new Image ("/org/humbertodelacruz/images/nuevo.png"));
                    imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
            }

        }
        
        public void guardar(){
            Administracion registro = new Administracion();
            registro.setDireccion(txtDireccion.getText());
            registro.setTelefono(txtTelefono.getText());      
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarAdministracion(?, ?)}");
                procedimiento.setString(1, registro.getDireccion());
                procedimiento.setString(2, registro.getTelefono());
                procedimiento.execute();
                //TODO LO QUE HICIMOS ANTERIORMENTE PASA EN MYSQL, ENTONCES CON ESTE CODIGO SUCEDE TAMBIEN EN LA TABLA
                listaAdministracion.add(registro); 
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void eliminar(){
            switch(tipoDeOperacion){
                case GUARDAR:
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    imgNuevo.setImage(new Image ("/org/humbertodelacruz/images/nuevo.png"));
                    imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
                
                default:
                    if(tblAdministracion.getSelectionModel().getSelectedItem() != null){
                        int respuesta=JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?" , "Eliminar Adminstración", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarAdministracion(?)}");
                                procedimiento.setInt(1, ((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion()); //getSelectionModel() = Indica que estamos selecionando una fila
                                procedimiento.execute();                                                                                                      //getSelectedItem() = Indica que fila especificamente seleccionamos
                                //TODO LO QUE HICIMOS ANTERIORMENTE PASA EN MYSQL, ENTONCES CON ESTE CODIGO SUCEDE TAMBIEN EN LA TABLA
                                listaAdministracion.remove(tblAdministracion.getSelectionModel().getSelectedIndex()); 
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
        
        
        public void seleccionarElemento(){
            
            if(tblAdministracion.getSelectionModel().getSelectedItem() != null){
                txtCodigoAdministracion.setText(String.valueOf(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
                txtDireccion.setText(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
                txtTelefono.setText(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
            }
            
            
        }
        
        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblAdministracion.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                        imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                    break;
                    
                case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    desactivarControles();
                    limpiarControles();
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
            }
        }
        
        public void actualizar(){
            try {
                PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_EditarAdministracion(?,?,?)}");
                Administracion registro = (Administracion)tblAdministracion.getSelectionModel().getSelectedItem();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());
                procedimiento.setInt(1, registro.getCodigoAdministracion());
                procedimiento.setString(2, registro.getDireccion());
                procedimiento.setString(3, registro.getTelefono());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
       public void reporte(){
           switch(tipoDeOperacion){
               case ACTUALIZAR:
                   desactivarControles();
                   limpiarControles();
                   btnEditar.setText("Editar");
                   btnReporte.setText("Reporte");
                   imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                   imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                   btnNuevo.setDisable(false);
                   btnEliminar.setDisable(false);
                   tipoDeOperacion = operaciones.NINGUNO;
                   break;
                   
                   
                case NINGUNO:
                    imprimirReporte();             
                    break;
                   
           }
       }
       
       
       public void imprimirReporte(){
           Map parametros = new HashMap();
           parametros.put("codigoAdministracion", null);
           GenerarReporte.mostrarReporte("ReporteAdministracion.jasper", "Reporte de Administración", parametros);
       }
       
       
        
        
        public void desactivarControles(){
            txtCodigoAdministracion.setEditable(false);
            txtDireccion.setEditable(false);
            txtTelefono.setEditable(false);           
        }
        
        public void activarControles(){
            txtCodigoAdministracion.setEditable(false);
            txtDireccion.setEditable(true);
            txtTelefono.setEditable(true);
        }
        
        public void limpiarControles(){
            txtCodigoAdministracion.clear();
            txtDireccion.clear();
            txtTelefono.clear();
        }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        this.escenarioPrincipal.menuPrincipal();
    }
    
}
