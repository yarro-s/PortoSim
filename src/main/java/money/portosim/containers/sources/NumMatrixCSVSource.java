/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.sources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.Matrix;
import money.portosim.containers.core.OrderedFrame;
import money.portosim.containers.core.Pair;
import money.portosim.containers.numeric.NumDataMatrix;
import money.portosim.containers.numeric.NumFrame;
import money.portosim.containers.numeric.NumMatrix;
import money.portosim.containers.numeric.NumOrderedFrame;

/**
 *
 * @author yarro
 */
public class NumMatrixCSVSource extends AbstractMap<Pair<Date, String>, Double>
        implements NumMatrix<Date, String> {

    private final FileReader fileReader;
    private NumMatrix<Date, String> numMatrix;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public NumMatrixCSVSource(FileReader fileReader) {
        this.fileReader = fileReader;
        readMap();
    }

    @Override
    public Set<Entry<Pair<Date, String>, Double>> entrySet() {
        readMap();
        return numMatrix.entrySet();
    }
    
    @Override
    public Frame<String, ? extends NumOrderedFrame<Date>> columns() {
        return numMatrix.columns();
    }

    @Override
    public OrderedFrame<Date, ? extends NumFrame<String>> rows() {
        return numMatrix.rows();
    }
    
    private void readMap() {
        numMatrix = new NumDataMatrix<>();
        try {
            //fileReader.reset();
            var reader = new BufferedReader(fileReader);

            var headerKeys = Arrays.asList(reader.readLine().split(","));
            while (true) {
                String record;
                if ((record = reader.readLine()) == null) {
                    break;
                } else {
                    var parsedRecord = Arrays.asList(record.split(","));
                    var idx = isoStringToDate(parsedRecord.get(0));
                        
                    IntStream.range(1, headerKeys.size()).forEach(k -> {
                        var key = headerKeys.get(k);
                        var keyPair = Pair.of(idx, key);
                        var value = Double.parseDouble((String) parsedRecord.get(k));

                        numMatrix.put(keyPair, value);
                    });
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NumMatrixCSVSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Date isoStringToDate(String isoDate) {
        Date date = null;
        try {
            date = formatter.parse(isoDate);
        } catch (ParseException ex) {
        }

        return date;
    }
}
