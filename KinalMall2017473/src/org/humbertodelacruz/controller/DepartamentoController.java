
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Departamentos;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;

public class DepartamentoController implements Initializable{

    private Principal escenarioPrincipal;
    private ObservableList <Departamentos> listaDepartamentos;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperacion =operaciones.NINGUNO;
    
    @FXML private Button btnNuevo;    
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblDepartamento;
    @FXML private TableColumn colCodigoDepartamento;
    @FXML private TableColumn colNombreDepartamento;
    @FXML private TextField txtCodigoDepartamento;
    @FXML private TextField txtNombreDepartamento;
  
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblDepartamento.setItems(getDepartamento());
        colCodigoDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("codigoDepartamento"));
        colNombreDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombreDepartamento"));
        
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
       txtCodigoDepartamento.setEditable(false);
       txtNombreDepartamento.setEditable(true);
   } 
   
   public void desactivarControles(){
       txtCodigoDepartamento.setEditable(false);
       txtNombreDepartamento.setEditable(false);
   }
   
   public void limpiarControles(){
       txtCodigoDepartamento.clear();
       txtNombreDepartamento.clear();
   }
    
   /* *****************************************************************************/


    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/guardar.png"));
                imgEliminar.setImage(new Image("org/humbertodelacruz/images/cancelar.png"));
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
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }

    }
    
    public void guardar(){
        Departamentos registro = new Departamentos();
        //Establcer los elementos a Guardar
        registro.setNombreDepartamento(txtNombreDepartamento.getText());
        try {
            PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDepartamento(?)}");
            procedimiento.setString(1, registro.getNombreDepartamento());
            procedimiento.execute();
            
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
/* *****************************************************************************/
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                imgEliminar.setImage((new Image("/org/humbertodelacruz/images/eliminar.png")));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
                
            default:
                if(tblDepartamento.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este elemento", "Eliminar Departamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDepartamento(?)}");
                            procedimiento.setInt(1, ((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem()).getCodigoDepartamento());
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
    
    public void seleccionarElementos(){
        if(tblDepartamento.getSelectionModel().getSelectedItem() !=null){
            txtCodigoDepartamento.setText(String.valueOf(((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
            txtNombreDepartamento.setText(((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem()).getNombreDepartamento());
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
 /* *****************************************************************************/
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDepartamento.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;  
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }break;
                
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
   /* *****************************************************************************/  
    public void actualizar(){
        try {
            //llamara al procedimiento
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDepartamento(?,?)}");
            //almcenar el elemento seleccionado
            Departamentos registro = (Departamentos) tblDepartamento.getSelectionModel().getSelectedItem();
            //Establcer los elementos a actualizar
            registro.setNombreDepartamento(txtNombreDepartamento.getText());
            //Ingresar paramteros al procedimiento
            procedimiento.setInt(1, registro.getCodigoDepartamento());
            procedimiento.setString(2,registro.getNombreDepartamento());
            //ejecutar procedimiento
            procedimiento.execute();
            cargarDatos();
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;

        }
    }
}
