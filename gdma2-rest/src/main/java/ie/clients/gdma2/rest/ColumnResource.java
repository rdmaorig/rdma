package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/column")
public class ColumnResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(ServerResource.class);

	@RequestMapping(value = "list")
	public List<Column> getAllColumns(){
		logger.debug("getAllColumns()");
		return serviceFacade.getMetadataService().getAllColumns();
	}
	
	@RequestMapping(value = "table/{id}/active")
	public List<Column> findByTableIdAndActiveTrue(@PathVariable("id") Integer tableId){
		logger.info("findByTableIdAndActiveTrue, tableId: " + tableId);
		return serviceFacade.getMetadataService().findByTableIdAndActiveTrue(tableId);
	}

	
	/*paginated active columns for Table - Admin module */ 
	@RequestMapping("/metadata/table/{id}")
	PaginatedTableResponse<Column> getPaginatedActiveSynchedColumnsForTable(@PathVariable("id") Integer tableId,
			@RequestParam Map<String, String> reqParams){
		logger.debug("getColumnsPaginatedTable");
		
		String orderByColumn = "id";
		
		switch (getOrderByColumn(reqParams)) {
		case 0:
			orderByColumn = "id";
			break;
		case 1:
			orderByColumn = "name";
			break;
		case 2: 
			orderByColumn = "columnType";
			break;
		case 3: 
			orderByColumn = "columnTypeString";
			break;
		case 4: 
			orderByColumn = "dropDownColumnDisplay.name";
			break;
		case 6:
			orderByColumn = "dropDownColumnStore.name";
			break;
		case 7:
			orderByColumn = "displayed";
			break;
		case 8:
			orderByColumn = "allowInsert";
			break;
		case 9:
			orderByColumn = "allowUpdate";
			break;
		case 10:
			orderByColumn = "nullable";
			break;
		case 11:
			orderByColumn = "primarykey";
			break;
		case 12:
			orderByColumn = "special";
			break;
		case 13:
			orderByColumn = "minWidth";
			break;
		case 14:
			orderByColumn = "maxWidth";
			break;
		case 15:
			orderByColumn = "orderby";
			break;
		case 16:
			orderByColumn = "columnSize";
			break;
		case 17:
			orderByColumn = "alias";
			break;
		}
		
		logger.info("orderByColumn: " + orderByColumn);
		PaginatedTableResponse<Column> resp = serviceFacade.getMetadataService().getActiveSynchedColumnsForTable(
				tableId,
				getSearchValue(reqParams),
				orderByColumn,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams));
		
		resp.setDraw(getDraw(reqParams));
		logger.debug("getColumnsPaginatedTable ended");
		return resp;
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public List<Column> saveColumns(@RequestBody List<Column> columnList){
		logger.debug("saveColumns");
		return serviceFacade.getMetadataService().saveColumns(columnList);
		
	}
	
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public void deleteColumn(@PathVariable("id") Integer columnId){
		logger.debug("deleteColumn " + columnId);
		serviceFacade.getMetadataService().deleteColumn(columnId);
	}
}
