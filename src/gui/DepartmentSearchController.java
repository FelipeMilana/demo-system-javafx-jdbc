package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentSearchController implements Initializable{
	
	//associations
	private Department entity;
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<DataChangeListener>();
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btFilter;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtFilterAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			//instantiating department from search
			entity = getSearchData();
			
			//use database to find this department
			Department dep = service.findDepartmentByName(entity);
			
			if(dep == null) {
				Alerts.showAlert("Error finding department", null, "No department was found", AlertType.ERROR);
				notifyDataChangeListeners();
			}
			
			else {
				//add to list
				List<Department> list = Arrays.asList(dep);
				
				//send the event, because the tableview was changed
				notifyDataChangeListeners(list);
			}
			
			//close window
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorsMessages(e.getErrors());
		}
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		//close window
		Utils.currentStage(event).close();
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateSearchData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtName.setText(entity.getName());
	}
	
	private Department getSearchData() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation error");
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	private void notifyDataChangeListeners(List<Department> list) {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChangedSearch(list);
		}
	}
	
	private void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
