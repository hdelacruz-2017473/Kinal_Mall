
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Horario;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;


public class HorarioController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR , ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Horario> listaHorario;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private  ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoHorario;
    @FXML private TextField txtHorarioEntrada;
    @FXML private TextField txtHorarioSalida;
    @FXML private CheckBox chbLunes;
    @FXML private CheckBox chbMartes;
    @FXML private CheckBox chbMiercoles;
    @FXML private CheckBox chbJueves;
    @FXML private CheckBox chbViernes;
    @FXML private TableView tblHorario;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colHorarioEntrada;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
     
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblHorario.setItems(getHorarios());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horario,Integer>("codigoHorario"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
        
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
        
        return listaHorario = FXCollections.observableArrayList(lista);
        
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
        txtCodigoHorario.setEditable(false);
        txtHorarioEntrada.setEditable(true);
        txtHorarioSalida.setEditable(true);
        chbLunes.setDisable(false);
        chbMartes.setDisable(false);
        chbMiercoles.setDisable(false);
        chbJueves.setDisable(false);
        chbViernes.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoHorario.setEditable(false);
        txtHorarioEntrada.setEditable(false);
        txtHorarioSalida.setEditable(false);
        chbLunes.setDisable(true);
        chbMartes.setDisable(true);
        chbMiercoles.setDisable(true);
        chbJueves.setDisable(true);
        chbViernes.setDisable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoHorario.clear();
        txtHorarioEntrada.clear();
        txtHorarioSalida.clear();
        chbLunes.setSelected(false);
        chbMartes.setSelected(false);
        chbMiercoles.setSelected(false);
        chbJueves.setSelected(false);
        chbViernes.setSelected(false);
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
        }
    }
    
    
    public void guardar (){
        Horario registro = new Horario();
        registro.setHorarioEntrada(txtHorarioEntrada.getText());
        registro.setHorarioSalida(txtHorarioSalida.getText());
        registro.setLunes(chbLunes.isSelected());
        registro.setMartes(chbMartes.isSelected());
        registro.setMiercoles(chbMiercoles.isSelected());
        registro.setJueves(chbJueves.isSelected());
        registro.setViernes(chbViernes.isSelected());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioEntrada());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            
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
                if(tblHorario.getSelectionModel().getSelectedItem() != null){
                    int resultado= JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el elemento?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horario)tblHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            
                            procedimiento.execute();
                            limpiarControles();
                            cargarDatos();
                            
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
        if(tblHorario.getSelectionModel().getSelectedItem() != null){
            txtCodigoHorario.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            txtHorarioEntrada.setText(((Horario)tblHorario.getSelectionModel().getSelectedItem()).getHorarioEntrada());
            txtHorarioSalida.setText(((Horario)tblHorario.getSelectionModel().getSelectedItem()).getHorarioSalida());
            chbLunes.setSelected(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isLunes());
            chbMartes.setSelected(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isMartes());
            chbMiercoles.setSelected(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isMiercoles());
            chbJueves.setSelected(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isJueves());
            chbViernes.setSelected(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isViernes());
           
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
/* *****************************************************************************/
    
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblHorario.getSelectionModel().getSelectedItem() != null){
                    
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
            break;
                
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
            break;       
        }
    }
    
    
    public void actualizar(){
        Horario registro = (Horario)tblHorario.getSelectionModel().getSelectedItem();
        registro.setHorarioEntrada(txtHorarioEntrada.getText());
        registro.setHorarioSalida(txtHorarioSalida.getText());
        registro.setLunes(chbLunes.isSelected());
        registro.setMartes(chbMartes.isSelected());
        registro.setMiercoles(chbMiercoles.isSelected());
        registro.setJueves(chbJueves.isSelected());
        registro.setViernes(chbViernes.isSelected());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarHorario(?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioEntrada());
            procedimiento.setString(3, registro.getHorarioSalida());
            procedimiento.setBoolean(4, registro.isLunes());
            procedimiento.setBoolean(5, registro.isMartes());
            procedimiento.setBoolean(6, registro.isMiercoles());
            procedimiento.setBoolean(7, registro.isJueves());
            procedimiento.setBoolean(8, registro.isViernes());
            
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
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                
            break;
                
        }
    }
    
    
    
    
  
    
}
