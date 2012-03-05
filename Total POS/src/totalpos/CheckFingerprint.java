
package totalpos;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.Color;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Saúl Hidalgo.
 */
public class CheckFingerprint extends fingerPrintReader{

    private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
    private List<FingerPrint> allMyFingerprints = null;
    public boolean isOk = false;

    public CheckFingerprint() {
        super();
        try {
            allMyFingerprints = ConnectionDrivers.getAllFingerPrints();
            super.setNameLabel("");
            super.setTitleLabel("");
            super.setState("");
            isOk = true;
        } catch (SQLException ex) {
            
        }
    }

    @Override
    protected void process(DPFPSample sample) {
        super.process(sample);

        DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        if (features != null) {
            // Compare the feature set with our template

            boolean isOk = false;
            for (FingerPrint f : allMyFingerprints) {
                DPFPTemplate t = DPFPGlobal.getTemplateFactory().createTemplate();
                t.deserialize(f.getBytesArray());
                DPFPVerificationResult result = verificator.verify(features, t );
                if (result.isVerified()){
                    try {
                        Employ e = ConnectionDrivers.getEmploy(f.getEmployId());
                        if ( e == null ){
                            super.setState("Empleado no registrado");
                            super.setTitleLabel("");
                            super.setNameLabel("");
                        }else{
                            String[] names = e.getName().split(",");
                            super.setState(ConnectionDrivers.saveFingerPrint(e) + " " + ConnectionDrivers.currentHour());
                            super.setTitleLabel(names[0]);
                            super.setNameLabel(names[1]);
                            super.setColorState(new Color(0, 182, 255));
                            isOk = true;
                        }
                        break;
                    } catch (SQLException ex) {
                        MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Error desconocido", ex);
                        msb.show(Shared.getMyMainWindows());
                    } catch (Exception ex) {
                        MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Error desconocido", ex);
                        msb.show(Shared.getMyMainWindows());
                    } 
                }
            }

            if (!isOk){
                super.setState("No Reconocido");
                super.setColorState(Color.RED);
                super.setTitleLabel("");
                super.setNameLabel("");
            }
            
                
        }else{
            super.setTitleLabel("Imagen");
            super.setColorState(Color.RED);
            super.setNameLabel("Mala Calidad");
            super.setState("");
        }
    }

    

}
