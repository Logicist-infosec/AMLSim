package amlsim.model.normal;

import amlsim.*;
import amlsim.model.AbstractTransactionModel;
import java.util.*;

/**
 * Send money from single source to multiple destinations (fan-out)
 */
public class FanOutTransactionModel extends AbstractTransactionModel {

    private int index = 0;

    public void setParameters(int interval, float balance, long start, long end){
        super.setParameters(interval, balance, start, end);
        if(this.startStep < 0){  // decentralize the first transaction step
            this.startStep = generateStartStep(interval);
        }
    }

    @Override
    public String getType() {
        return "FanOut";
    }

    private boolean isValidStep(long step){
        return (step - startStep) % interval == 0;
    }

    @Override
    public void sendTransaction(long step) {
        List<Account> beneList = this.account.getBeneList();  // Destination accounts
        int numBene = beneList.size();
        if(!isValidStep(step) || numBene == 0){  // No more destination accounts
            return;
        }
        if(index >= numBene){
            index = 0;
        }

        float amount = getTransactionAmount();
        Account dest = beneList.get(index);
        this.sendTransaction(step, amount, dest);
        index++;
    }
}
