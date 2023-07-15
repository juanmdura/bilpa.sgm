package app.client.iu.rightClick;

import java.util.ArrayList;
import java.util.List;

import app.client.utilidades.utilObjects.SortableTable;

import com.google.gwt.user.client.ui.SourcesTableEvents;

public class AdvSortableTable extends SortableTable{

	// Holds the data rows of the table
	// This is a list of RowData Object
	protected List<AdvRowData> tableRows = new ArrayList<AdvRowData>();
	
	public AdvSortableTable() {}

	
	
	@Override
	public void onCellClicked(SourcesTableEvents sender, int row, int col) {
		
	}



	public void setValue(int rowIndex, int colIndex, Comparable<?> label) {
		AdvLabel advLabel = (AdvLabel)label;
		// The rowIndex should begin with 1 as rowIndex 0 is for the Header
		// Any row with index == 0 will not be displayed.
		if(rowIndex == 0){
			return;
		}

		if((rowIndex-1) >= this.tableRows.size() || null == tableRows.get(rowIndex-1)){
			tableRows.add(rowIndex-1, new AdvRowData(advLabel));
		}

		final RowData rowData = tableRows.get(rowIndex-1); 
		rowData.addColumnValue(colIndex, advLabel.getText());
		
		this.setWidget(rowIndex, colIndex, advLabel );
	}

	@Override
	protected void displayTableBody() {
//		IUGestionarModelo.dibujarTabla();
		
		if(this.sortDirection == SORT_ASC || this.sortDirection == -1){
			// Ascending order and Default Display
			for(int rowIndex=0; rowIndex<tableRows.size(); rowIndex++){
				final AdvRowData cell = tableRows.get(rowIndex);
		
				for(int colIndex=0; colIndex<cell.getColumnValues().size(); colIndex++){
					final Object value = cell.getColumnValue(colIndex);
				
					if(null != value){
						cell.label.setText(value.toString());
						this.setWidget(rowIndex+1, colIndex, cell.label);
					}
				}
			}
		}else{
			// Descending Order Display
			for(int rowIndex=tableRows.size()-1, rowNum = 1; rowIndex>=0; rowIndex--, rowNum++){
				final AdvRowData cell = tableRows.get(rowIndex);
				
				for(int colIndex=0; colIndex<cell.getColumnValues().size(); colIndex++){
					final Object value = cell.getColumnValue(colIndex);
				
					if(null != value){
						cell.label.setText(value.toString());
						this.setWidget(rowNum, colIndex,  cell.label);
					}
				}
			}
		}
	}
	
	
	/*
	 * RowData defines one row in a Sortable Table
	 */
	public class AdvRowData extends RowData implements Comparable<Object> {
		
		public AdvRowData(AdvLabel label) {
			this.label = label;
		}

		
		AdvLabel label;
	}
	
}
