
package totalpos;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                        String[] names = ConnectionDrivers.getEmploy(f.getEmployId()).getName().split(",");
                        super.setState("Aceptado");
                        super.setTitleLabel(names[0]);
                        super.setNameLabel(names[1]);
                        isOk = true;
                        break;
                    } catch (SQLException ex) {
                        MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Error desconocido", ex);
                        msb.show(Shared.getMyMainWindows());
                    }
                }
            }

            if (!isOk){
                super.setState("No Reconocido");
                super.setTitleLabel("");
                super.setNameLabel("");
            }
            
                
        }else{
            super.setTitleLabel("IMAGEN DE MALA CALIDAD!");
            super.setNameLabel("");
        }
    }

    

}
