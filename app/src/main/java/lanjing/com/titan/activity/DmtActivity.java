package lanjing.com.titan.activity;

import android.os.Bundle;

import com.lxh.baselibray.mvp.MvpActivity;

import lanjing.com.titan.R;
import lanjing.com.titan.contact.AgreementContact;
import lanjing.com.titan.response.AgreementResponse;
import retrofit2.Response;

public class DmtActivity extends MvpActivity<AgreementContact.AgreementPresent> implements AgreementContact.IAgreementView {
    @Override
    protected AgreementContact.AgreementPresent createPresent() {
        return new AgreementContact.AgreementPresent();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dmt;
    }

    @Override
    public void getAgreementResult(Response<AgreementResponse> response) {

    }

    @Override
    public void getDataFailed() {

    }
}
