/*
 * fingerPrintReader.java
 *
 * Created on 28-feb-2012, 15:45:01
 */

package totalpos;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorListener;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 *
 * @author Saúl Hidalgo.
 */
public class fingerPrintReader extends javax.swing.JInternalFrame {

    private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();

    /** Creates new form fingerPrintReader */
    public fingerPrintReader() {
        initComponents();

        this.addComponentListener(new ComponentAdapter() {
            @Override public void componentShown(ComponentEvent e) {
                init();
                start();
            }
            @Override public void componentHidden(ComponentEvent e) {
                stop();
            }

        });
    }

    protected void init(){
        capturer.addDataListener(new DPFPDataAdapter() {
            @Override public void dataAcquired(final DPFPDataEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    logThis("Capturada una muestra");
                    process(e.getSample());
                }});
            }
        });
        capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
            @Override public void readerConnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    logThis("Capta Huellas conectado");
                }});
            }
            @Override public void readerDisconnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    logThis("Capta Huellas desconectado");
                }});
            }
        });
        capturer.addSensorListener((DPFPSensorListener) new DPFPSensorAdapter() {
            @Override public void fingerTouched(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    // Capta Huellas tocado.
                }});
            }
            @Override public void fingerGone(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    //
                }});
            }
        });
        capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
            @Override public void onImageQuality(final DPFPImageQualityEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    /*if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
                        logThis("Calidad de la im");
                    else
                        logThis("The quality of the fingerprint sample is poor.");*/
                }});
            }
        });
    }

    protected void logThis(String msg){
        //textAreaLog.append(msg + "\n");
    }

    protected void process( DPFPSample sample ){
        Image img = DPFPGlobal.getSampleConversionFactory().createImage(sample);
        picture.setIcon(new ImageIcon( img.getScaledInstance(picture.getWidth(), picture.getHeight() , Image.SCALE_FAST))) ;
    }

    protected void start(){
        capturer.startCapture();
    }

    protected void stop(){
        capturer.stopCapture();
    }

    protected void setState(String msg){
        operationLabel.setText(msg);
    }

    protected void setTitleLabel(String msg){
        titleLabel.setText(msg);
    }

    protected void setNameLabel(String msg){
        employName.setText(msg);
    }

    protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose){
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        employName = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        picture = new javax.swing.JLabel();
        operationLabel = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Capta Huellas");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 36));
        titleLabel.setText("HIDALGO AULAR");
        titleLabel.setName("titleLabel"); // NOI18N

        employName.setFont(new java.awt.Font("Courier New", 1, 36));
        employName.setText("SAUL SAMIR");
        employName.setName("employName"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        picture.setName("picture"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(picture, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(picture, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );

        operationLabel.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        operationLabel.setText("SALIDA O ENTRADA");
        operationLabel.setName("operationLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                    .addComponent(employName, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                    .addComponent(operationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(operationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(employName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public void setFontSize2Name(int n){
        employName.setFont(new java.awt.Font("Courier New", 1, n));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel employName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel operationLabel;
    private javax.swing.JLabel picture;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
