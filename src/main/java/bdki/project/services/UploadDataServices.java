package bdki.project.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bdki.project.entity.UploadData;
import bdki.project.repository.UploadDataRepository;

@Service
public class UploadDataServices {

    private final UploadDataRepository uploadDataRepository;

    @Autowired
    public UploadDataServices(UploadDataRepository uploadDataRepository) {
        this.uploadDataRepository = uploadDataRepository;
    }

    public List<String> readXlsxFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            List<String> jsonDataList = new ArrayList<>();
            Row headerRow = rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                StringBuilder rowData = new StringBuilder("{");

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    String columnName = headerRow.getCell(columnIndex).getStringCellValue();
                    String cellValue = getCellValueAsString(cell);
                    rowData.append("\"").append(columnName).append("\":\"").append(cellValue).append("\",");
                }
                rowData.deleteCharAt(rowData.length() - 1);
                rowData.append("}");
                jsonDataList.add(rowData.toString());
            }

            // Save JSON data to database
            List<UploadData> excelDataList = new ArrayList<>();
            for (String jsonData : jsonDataList) {
                UploadData uploadData = new UploadData();
                uploadData.setDataJson(jsonData);
                excelDataList.add(uploadData);
            }
            uploadDataRepository.saveAll(excelDataList);
            System.out.println("ini data yang disimpan : " + excelDataList);



            return jsonDataList;
        }
    }

    // Utility method to get cell value as string
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
