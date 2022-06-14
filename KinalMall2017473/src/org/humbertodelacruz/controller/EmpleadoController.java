
package org.humbertodelacruz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Administracion;
import org.humbertodelacruz.bean.Cargo;
import org.humbertodelacruz.bean.Departamentos;
import org.humbertodelacruz.bean.Empleado;
import org.humbertodelacruz.bean.Horario;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.report.GenerarReporte;
import org.humbertodelacruz.system.Principal;


public class EmpleadoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Empleado> listaEmpleados;
    private ObservableList<Departamentos> listaDepartamentos;
    private ObservableList<Cargo> listaCargos;
    private ObservableList <Horario> listaHorarios;
    private ObservableList <Administracion> listaAdministracion;
    private DatePicker fechaContratacion;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private  ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblEmpleado;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNombreEmpleado;
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colCorreoElectronico;
    @FXML private TableColumn colTelefonoEmpleado;
    @FXML private TableColumn colFechaContratacion;
    @FXML private TableColumn colSueldo;
    @FXML private TableColumn colCodigoDepartamento;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtCorreoElectronico;
    @FXML private TextField txtTelefonoEmpleado;
    @FXML private TextField txtSueldo;
    @FXML private GridPane grpFechaContratacion;
    @FXML private ComboBox cmbCodigoDepartamento;
    @FXML private ComboBox cmbCodigoCargo;
    @FXML private ComboBox cmbCodigoHorario;
    @FXML private ComboBox cmbCodigoAdministracion;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fechaContratacion = new DatePicker(Locale.ENGLISH);
        fechaContratacion.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaContratacion.getCalendarView().todayButtonTextProperty().set("Today");
        fechaContratacion.getCalendarView().setShowWeeks(false);
        
        grpFechaContratacion.add(fechaContratacion, 5, 0);
        fechaContratacion.getStylesheets().add("/org/humbertodelacruz/resource/DatePicker.css");
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer> ("codigoEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("nombreEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("apellidoEmpleado"));
        colCorreoElectronico.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("correoElectronico"));
        colTelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String> ("telefonoEmpleado"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory<Empleado, Date> ("fechaContratacion"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double> ("sueldo"));
        colCodigoDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer> ("codigoDepartamento"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer> ("codigoCargo"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer> ("codigoHorario"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer> ("codigoAdministracion"));
        cmbCodigoDepartamento.setItems(getDepartamento());
        cmbCodigoCargo.setItems(getCargos());
        cmbCodigoHorario.setItems(getHorarios());
        cmbCodigoAdministracion.setItems(getAdministracion());

    }

    public ObservableList<Empleado> getEmpleados(){
        
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"), 
                                        resultado.getString("nombreEmpleado"), 
                                        resultado.getString("apellidoEmpleado"), 
                                        resultado.getString("correoElectronico"), 
                                        resultado.getString("telefonoEmpleado"), 
                                        resultado.getDate("fechaContratacion"), 
                                        resultado.getDouble("sueldo"), 
                                        resultado.getInt("codigoDepartamento"), 
                                        resultado.getInt("codigoCargo"), 
                                        resultado.getInt("codigoHorario"), 
                                        resultado.getInt("codigoAdministracion")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList<Departamentos> getDepartamento(){
        ArrayList <Departamentos> lista = new ArrayList<Departamentos>();
        try {
            PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
            ResultSet resultado =procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Departamentos(resultado.getInt("codigoDepartamento"), resultado.getString("nombreDepartamento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDepartamentos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Cargo> getCargos(){
        ArrayList <Cargo> lista = new ArrayList<Cargo>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"), 
                                    resultado.getString("nombreCargo")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCargos = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList<Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"), 
                                        resultado.getString("horarioEntrada"), 
                                        resultado.getString("horarioSalida"), 
                                        resultado.getBoolean("lunes"), 
                                        resultado.getBoolean("martes"), 
                                        resultado.getBoolean("miercoles"), 
                                        resultado.getBoolean("jueves"), 
                                        resultado.getBoolean("viernes")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaHorarios = FXCollections.observableArrayList(lista);
        
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
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtCorreoElectronico.setEditable(true);
        txtTelefonoEmpleado.setEditable(true);
        fechaContratacion.setDisable(false);
        txtSueldo.setEditable(true);
        cmbCodigoDepartamento.setDisable(false);
        cmbCodigoCargo.setDisable(false);
        cmbCodigoHorario.setDisable(false);
        cmbCodigoAdministracion.setDisable(false); 
    }
     
        public void activarControles2(){
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtCorreoElectronico.setEditable(true);
        txtTelefonoEmpleado.setEditable(true);
        fechaContratacion.setDisable(false);
        txtSueldo.setEditable(true);
        cmbCodigoDepartamento.setDisable(true);
        cmbCodigoCargo.setDisable(true);
        cmbCodigoHorario.setDisable(true);
        cmbCodigoAdministracion.setDisable(true);
    }
    
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtCorreoElectronico.setEditable(false);
        txtTelefonoEmpleado.setEditable(false);
        fechaContratacion.setDisable(true);
        txtSueldo.setEditable(false);
        cmbCodigoDepartamento.setDisable(true);
        cmbCodigoCargo.setDisable(true);
        cmbCodigoHorario.setDisable(true);
        cmbCodigoAdministracion.setDisable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtCorreoElectronico.clear();
        txtTelefonoEmpleado.clear();
        fechaContratacion.setPromptText("");
        txtSueldo.clear();
        cmbCodigoDepartamento.setValue(null);
        cmbCodigoCargo.setValue(null);
        cmbCodigoHorario.setValue(null);
        cmbCodigoAdministracion.setValue(null);
        
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
        Empleado registro = new Empleado();
        registro.setNombreEmpleado(txtNombreEmpleado.getText());
        registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
        registro.setCorreoElectronico(txtCorreoElectronico.getText());
        registro.setTelefonoEmpleado(txtTelefonoEmpleado.getText());
        registro.setFechaContratacion(fechaContratacion.getSelectedDate());
        registro.setSueldo((Double.parseDouble(txtSueldo.getText())));
        registro.setCodigoDepartamento(((Departamentos)cmbCodigoDepartamento.getSelectionModel().getSelectedItem()).getCodigoDepartamento());
        registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        registro.setCodigoHorario(((Horario)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreEmpleado());
            procedimiento.setString(2, registro.getApellidoEmpleado());
            procedimiento.setString(3, registro.getCorreoElectronico());
            procedimiento.setString(4, registro.getTelefonoEmpleado());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaContratacion().getTime()));
            procedimiento.setDouble(6, registro.getSueldo());
            procedimiento.setInt(7, registro.getCodigoDepartamento());
            procedimiento.setInt(8, registro.getCodigoCargo());
            procedimiento.setInt(9, registro.getCodigoHorario());
            procedimiento.setInt(10, registro.getCodigoAdministracion());
            
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    int resultado = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Elemento?", "Elminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            
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
        if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
            txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNombreEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombreEmpleado());
            txtApellidoEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
            txtCorreoElectronico.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCorreoElectronico());
            txtTelefonoEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoEmpleado());
            fechaContratacion.selectedDateProperty().set(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getFechaContratacion());
            txtSueldo.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getSueldo()));
            cmbCodigoDepartamento.getSelectionModel().select(buscarDepartamento(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
            cmbCodigoCargo.getSelectionModel().select(buscarCargo(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            cmbCodigoHorario.getSelectionModel().select(buscarHorario(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public Departamentos buscarDepartamento(int codigoDepartamento){
        Departamentos resultado = null;
        try {
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDepartamento(?)}");
            procedimeinto.setInt(1, codigoDepartamento);
            ResultSet registro = procedimeinto.executeQuery();
            while(registro.next()){
                resultado = new Departamentos(registro.getInt("codigoDepartamento"), 
                                registro.getString("nombreDepartamento"));               
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try {
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimeinto.setInt(1, codigoCargo);
            ResultSet registro = procedimeinto.executeQuery();
            while(registro.next()){
                resultado = new Cargo(registro.getInt("codigoCargo"), 
                                    registro.getString("nombreCargo"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public Horario buscarHorario(int codigoHorario){
        Horario resultado = null;
        try {
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarHorario(?)}");
            procedimeinto.setInt(1, codigoHorario);
            ResultSet registro = procedimeinto.executeQuery();
            while(registro.next()){
                resultado = new Horario(registro.getInt("codigoHorario"), 
                                        registro.getString("horarioEntrada"), 
                                        registro.getString("horarioSalida"), 
                                        registro.getBoolean("lunes"), 
                                        registro.getBoolean("martes"), 
                                        registro.getBoolean("miercoles"), 
                                        registro.getBoolean("jueves"), 
                                        registro.getBoolean("viernes"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
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

    
/* *****************************************************************************/

    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblEmpleado.getSelectionModel().getSelectedItem()!= null){
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
        Empleado registro = ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem());
        registro.setNombreEmpleado(txtNombreEmpleado.getText());
        registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
        registro.setCorreoElectronico(txtCorreoElectronico.getText());
        registro.setTelefonoEmpleado(txtTelefonoEmpleado.getText());
        registro.setFechaContratacion(fechaContratacion.getSelectedDate());
        registro.setSueldo((Double.parseDouble(txtSueldo.getText())));
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombreEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setString(4, registro.getCorreoElectronico());
            procedimiento.setString(5, registro.getTelefonoEmpleado());
            procedimiento.setDate(6,new java.sql.Date(registro.getFechaContratacion().getTime()));
            procedimiento.setDouble(7, registro.getSueldo());
            
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
                   
                   
                case NINGUNO:
                    if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                        imprimirReporte();             
                    
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un Elemento");
                    }
                break;    
                   
           }
       }
    
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codEmpleado", ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        GenerarReporte.mostrarReporte("ReporteEmpleado.jasper", "Reporte de Empleado", parametros);
        
    }




    
    
}
