
package totalpos;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;

/**
 *
 * @author shidalgo
 */
public class createCapture extends fingerPrintReader{

    private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    private String employId = null;

    public createCapture(String employId) {
        super();
        super.setState("Captura de Nueva Huella");
        super.setNameLabel("");
        updateStatus();
        this.employId = employId;
    }

    @Override
    protected void process(DPFPSample sample) {
        super.process(sample);
        DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

        try {
            if ( features != null ){
                enroller.addFeatures(features);            
            }
        } catch (DPFPImageQualityException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "No se ha guardado la huella", ex);
            msb.show(this);
        }finally{
            updateStatus();


            switch(enroller.getTemplateStatus()){
                case TEMPLATE_STATUS_READY:
                    stop();

                    // TODO GUARDAR EN BASE DE DATOS.

                    MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado satisfactoriamente");
                    msb.show(this);
                    this.dispose();
                break;

                case TEMPLATE_STATUS_FAILED:
                    enroller.clear();
                    stop();
                    updateStatus();
                    start();
                break;
            }
        }

    }

    private void updateStatus() {
        int n = enroller.getFeaturesNeeded();
        if ( n == 0 ){
            super.setTitleLabel("Listo");
        }else{
            super.setTitleLabel("Faltan " + enroller.getFeaturesNeeded() + " Captaciones.");
        }
    }

    

}
