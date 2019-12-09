package lanjing.com.titan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lxh.baselibray.mvp.MvpActivity;
import com.lxh.baselibray.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import lanjing.com.titan.R;
import lanjing.com.titan.constant.Constant;
import lanjing.com.titan.contact.AtnContact;
import lanjing.com.titan.response.AgreementResponse;
import lanjing.com.titan.response.HistoryListResponse;
import lanjing.com.titan.response.WalletDetailResponse;
import lanjing.com.titan.util.MoneyUtil;
import retrofit2.Response;
import retrofit2.http.Body;

/**
 * ATN资产
 */

public class AtnActivity extends MvpActivity<AtnContact.AtnPresent> implements AtnContact.IAtntView {

    @BindView(R.id.tv_zhican)
    TextView tv_zhican;
    @BindView(R.id.tv_withdrawal)
    TextView tv_withdrawal;
    @BindView(R.id.tv_btn_withdrawal)
    TextView tv_btn_withdrawal;
    @BindView(R.id.tv_btn_Purchase)
    TextView tv_btn_Purchase;
    @BindView(R.id.tv_btn_accounts)
    TextView tv_btn_accounts;
    @BindView(R.id.tv_Frozen)
    TextView tv_Frozen;

    String coin;

    @Override
    protected AtnContact.AtnPresent createPresent() {
        return new AtnContact.AtnPresent();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //获取数据
        coin = getIntent().getStringExtra("coin");
        mPresent.walletDetail(context, coin);

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_atn;
    }

    //点击事件
    @OnClick({R.id.tv_btn_withdrawal, R.id.tv_btn_Purchase, R.id.tv_btn_accounts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_withdrawal:
                ToastUtils.showLongToast(context, "提币按钮");
                break;
            case R.id.tv_btn_Purchase:
                ToastUtils.showLongToast(context, "买入按钮");
                break;
            case R.id.tv_btn_accounts:
                ToastUtils.showLongToast(context, "转账");
                break;

        }
    }


    @Override
    public void getWalletDeatilResult(Response<WalletDetailResponse> response) {
        if (response.body().getCode() == Constant.SUCCESS_CODE) {
            tv_zhican.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum()) +
                    response.body().getData().getWellet().getCointype());//资产余额
            tv_Frozen.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getFrozennum()));//交易冻结
            tv_withdrawal.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum()));//可提现余额
        } else {
            ToastUtils.showLongToast(context, response.body().getMsg());
        }

    }

    @Override
    public void gethistorylist(Response<HistoryListResponse> response) {

    }

    @Override
    public void getDataFailed() {
        ToastUtils.showLongToast(context, getResources().getString(R.string.network_error));//提示请求超时

    }
}
