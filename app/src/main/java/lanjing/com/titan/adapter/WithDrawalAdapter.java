package lanjing.com.titan.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lanjing.com.titan.R;
import lanjing.com.titan.response.CoinLogListResponse;
import lanjing.com.titan.util.DateUtils;
import lanjing.com.titan.util.MoneyUtil;

/**
 * Created by chenxi on 2019/5/24.
 * 充值提现  列表适配器
 */

public class WithDrawalAdapter extends BaseQuickAdapter<CoinLogListResponse.Data, BaseViewHolder> {

    public WithDrawalAdapter(int layoutResId, @Nullable List<CoinLogListResponse.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinLogListResponse.Data item) {
        int typeMoney = Integer.parseInt(item.getChangeCoin());
        int type = Integer.parseInt(item.getChangeType());
        switch (typeMoney) {
            case 1:
                helper.setText(R.id.tv_currency, " TITAN");
                helper.setText(R.id.tv_typemonet, " TITAN");
                break;
            case 2:
                helper.setText(R.id.tv_currency, " TITANC");
                helper.setText(R.id.tv_typemonet, " TITANC");
                break;
            case 5:
                helper.setText(R.id.tv_currency, " BAR");
                helper.setText(R.id.tv_typemonet, " BAR");
                break;
            case 6:
                helper.setText(R.id.tv_currency, " TRH");
                helper.setText(R.id.tv_typemonet, " TRH");
                break;
            case 7:
                helper.setText(R.id.tv_currency, " TRHC");
                helper.setText(R.id.tv_typemonet, " TRHC");
                break;
            case 8:
                helper.setText(R.id.tv_currency, " ATN");
                helper.setText(R.id.tv_typemonet, " ATN");
                break;
            case 9:
                helper.setText(R.id.tv_currency, " DMT");
                helper.setText(R.id.tv_typemonet, " DMT");
                break;
        }
        helper.setText(R.id.tv_deposit_type, item.getChangeDesc());
        helper.setText(R.id.tv_number, MoneyUtil.formatFour(item.getChangeAmount()));
        String time = DateUtils.timedate(item.getChangeTime());//时间戳转换成时间格式
        helper.setText(R.id.tv_create_time, time);
        helper.setText(R.id.tv_number, MoneyUtil.formatFour(item.getChangeAmount()));

    }
}
