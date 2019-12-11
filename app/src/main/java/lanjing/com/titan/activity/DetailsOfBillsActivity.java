package lanjing.com.titan.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lxh.baselibray.mvp.MvpActivity;
import com.lxh.baselibray.util.ToastUtils;

import butterknife.BindView;
import lanjing.com.titan.R;
import lanjing.com.titan.constant.Constant;
import lanjing.com.titan.contact.BillDetailContact;
import lanjing.com.titan.response.BillDetailResponse;
import lanjing.com.titan.response.SellOrderDetailResponse;
import lanjing.com.titan.util.DateUtils;
import lanjing.com.titan.util.MoneyUtil;
import retrofit2.Response;

public class DetailsOfBillsActivity extends MvpActivity<BillDetailContact.BillDetailPresent> implements BillDetailContact.IBillDetailView {


    @BindView(R.id.tv_titan_type)
    TextView tv_titan_type;//花费数量
    @BindView(R.id.tv_titan_state)
    TextView tv_titan_state;//手续费
    @BindView(R.id.tv_ource_address)
    TextView tv_ource_address;//成交时bar单间
    @BindView(R.id.tv_titan_address)
    TextView tv_titan_address;//获得BAR数量
    @BindView(R.id.tv_deal_one)
    TextView tv_deal_one;//成交时TITAN单价
    @BindView(R.id.tv_gettitansun)
    TextView tv_gettitansun;//获得TITAN数量
    @BindView(R.id.tv_titan_time)
    TextView tv_titan_time;//时间
    @BindView(R.id.tv_company)
    TextView tv_company;//文字提示
    @BindView(R.id.tv_price)
    TextView tv_price;

    String id;

    @Override
    protected BillDetailContact.BillDetailPresent createPresent() {
        return new BillDetailContact.BillDetailPresent();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        mPresent.SellOrderDetail(context, id);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_details_of_bills;
    }

    @Override
    public void getBillDeatilResult(Response<BillDetailResponse> response) {

    }

    //币币交易详情数据  coin1Price
    @Override
    public void getSellOrderDetail(Response<SellOrderDetailResponse> response) {
        if (response.body().getCode() == Constant.SUCCESS_CODE) {
            String price = "成交时" + response.body().getData().getCoin1Name() + getResources().getString(R.string.prices);
            tv_price.setText(price);//成交时单价
            String company = getResources().getString(R.string.get) + response.body().getData().getCoin1Name() + getResources().getString(R.string.Qty);
            tv_company.setText(company);//获得币种数量
            String titan_type = MoneyUtil.formatFour(String.valueOf(response.body().getData().getTradeAmount() + response.body().getData().getTradeFee())) + "\rUSD";
            tv_titan_type.setText(titan_type);//花费数量
            String titan_state = MoneyUtil.formatFour(String.valueOf(response.body().getData().getTradeFee())) + "\rUSD";
            tv_titan_state.setText(titan_state);//手续费
            String titan_address = MoneyUtil.formatFour(String.valueOf(response.body().getData().getGainCoin2Amount())) + "\rUSD";
            tv_titan_address.setText(titan_address);//获得BAR数量
            String deal_one = MoneyUtil.formatFour(String.valueOf(response.body().getData().getCoin1Price())) + "\rUSD";
            tv_deal_one.setText(deal_one);//成交时TITAN单价
            String gettitansun = MoneyUtil.formatFour(String.valueOf(response.body().getData().getGainCoin1Amount())) + "\r" + response.body().getData().getCoin1Name();
            tv_gettitansun.setText(gettitansun);//获得TITAN数量
            tv_titan_time.setText(DateUtils.timedate(response.body().getData().getCreateTime()));//时间
        } else {
            ToastUtils.showShortToast(context, response.body().getMsg());
        }

    }

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, getResources().getString(R.string.network_error));

    }
}
