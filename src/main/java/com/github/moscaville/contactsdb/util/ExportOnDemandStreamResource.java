/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.util;

import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.util.OnDemandFileDownloader.OnDemandStreamResource;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Grid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author moscac
 */
public class ExportOnDemandStreamResource implements OnDemandStreamResource {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object[] FILE_HEADER = {"Company","FirstName","LastName",
        "Address","City","State","Zip","Cell","Work","Email"};
    private final Grid grid;
    private final String fileName;

    public ExportOnDemandStreamResource(Grid grid) {
        this.grid = grid;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        fileName = "contactdb-" + sdf.format(new Date()) + ".csv";
    }

    @Override
    public String getFilename() {
        return fileName;
    }

    @Override
    public InputStream getStream() {
        exportFile(System.getProperty("java.io.tmpdir") + getFilename(), grid);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(System.getProperty("java.io.tmpdir") + getFilename()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportOnDemandStreamResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fis;
    }

    private static void exportFile(String fileName, Grid grid) {

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        
        Collection<Object> ids;
        if (grid.getSelectedRows().size() > 0) {
            ids = grid.getSelectedRows();
        } else {
            ids = (Collection<Object>) grid.getContainerDataSource().getItemIds();
        }

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;

        try {
            fileWriter = new FileWriter(fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);
            for (Object id : ids) {
                ContactRecord contact = (ContactRecord)((BeanItem)grid.getContainerDataSource().getItem(id)).getBean();
                List contactDataRecord = new ArrayList();
                
                contactDataRecord.add(contact.getCompanyName());
                contactDataRecord.add(contact.getFirstName());
                contactDataRecord.add(contact.getLastName());
                contactDataRecord.add(contact.getAddress());
                contactDataRecord.add(contact.getCity());
                contactDataRecord.add(contact.getState());
                contactDataRecord.add(contact.getZip());
                contactDataRecord.add(contact.getCellPhone());
                contactDataRecord.add(contact.getWorkPhone());
                contactDataRecord.add(contact.getEmail());

                csvFilePrinter.printRecord(contactDataRecord);
            }
        } catch (Exception e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
            }

        }
    }

}
