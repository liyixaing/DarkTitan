package lanjing.com.titan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxh.baselibray.dialog.AlertDialog;
import com.lxh.baselibray.mvp.MvpActivity;
import com.lxh.baselibray.util.ObjectUtils;
import com.lxh.baselibray.util.ToastUtils;
import com.lxh.baselibray.view.TitleView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lanjing.com.titan.R;
import lanjing.com.titan.adapter.CoinTitanAdapter;
import lanjing.com.titan.constant.Constant;
import lanjing.com.titan.contact.WalletDetailContact;
import lanjing.com.titan.response.HistoryListResponse;
import lanjing.com.titan.response.WalletDetailResponse;
import lanjing.com.titan.util.MoneyUtil;
import lanjing.com.titan.util.RecyclerViewAnimation;
import retrofit2.Response;

/**
 * TITAN  资产
 */
public class AssetTITANActivity extends MvpActivity<WalletDetailContact.WalletDetailPresent> implements WalletDetailContact.IWalletDetailView {

    @BindView(R.id.tv_asset_balance)
    TextView tvAssetBalance;
    @BindView(R.id.tv_tixian_balance)
    TextView tvTixianBalance;
    @BindView(R.id.tv_titan_trading_frozen)
    TextView tvTitanTradingFrozen;
    @BindView(R.id.tv_titan_screen)
    TextView tvTitanScreen;
    @BindView(R.id.rv_normal_show)
    LinearLayout rvNormalShow;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.top_up_c_btn)
    TextView topUpCBtn;
    @BindView(R.id.withdraw_c_btn)
    TextView withdrawCBtn;
    @BindView(R.id.exchange_btn)
    TextView exchangeBtn;
    @BindView(R.id.bottom_lay)
    LinearLayout bottomLay;
    @BindView(R.id.title_lay)
    TitleView TitleLay;
    @BindView(R.id.tv_exchange)
    TextView TvExchange;
    @BindView(R.id.ll_exchange)
    LinearLayout LlExchange;
    @BindView(R.id.tv_cash)
    TextView TvCash;
    @BindView(R.id.tv_zhican)
    TextView TvZhican;
    String walletId;//钱包ID
    CoinTitanAdapter mAdapter;
    List<HistoryListResponse.mData> historylist;
    int page = 1;
    int pageSize = 20;
    String type;//0，手续费 1，交易释放 2，充币 3，提币 4，买入 5，卖出 6，系统 7，其他 （不填写为全部）
    String types;//接收上个界面传来的类型（用来判断是哪一个界面跳转过来的）
    String suntaitan;
    String coin;//接收类型

    String sun = "0";

    @Override
    public void initData(Bundle savedInstanceState) {
        walletId = getIntent().getStringExtra("walletId");
        types = getIntent().getStringExtra("type");
        coin = getIntent().getStringExtra("coin");
        Log.e("asdasdas", coin);
        if (coin.equals("6")) {
            LlExchange.setVisibility(View.GONE);
        } else if (coin.equals("5")) {
            LlExchange.setVisibility(View.GONE);
            TvZhican.setVisibility(View.GONE);
            withdrawCBtn.setVisibility(View.GONE);
            tvTitanTradingFrozen.setVisibility(View.GONE);
            TvCash.setText(getResources().getString(R.string.frozen));
        } else if (coin.equals("8")) {
            topUpCBtn.setText(getResources().getString(R.string.turn_out));//修改文本为转出
            LlExchange.setVisibility(View.GONE);
        } else if (coin.equals("9")) {
            LlExchange.setVisibility(View.GONE);
//            withdrawCBtn.setVisibility(View.GONE);//取消隐藏提币
            withdrawCBtn.setText(getResources().getString(R.string.flash_exchange));//闪兑标题

        }
        TitleLay.setTitleText(types);

        //判断来源
        if (types.equals("TRH")) {//从泰坦资产跳转过来

        } else if (types.equals("BRA")) {//从bra界面跳转过来
            withdrawCBtn.setVisibility(View.GONE);
        } else {
            //不隐藏
        }
        historylist = new ArrayList<>();
        mAdapter = new CoinTitanAdapter(R.layout.recy_item_titan_list, historylist);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rv.setLayoutManager(manager);
        rv.setAdapter(mAdapter);
        mPresent.walletDetail(context, coin);
        mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //判断是否要进入详情页  1 充币  2 提币  21 titan兑换  22 bar兑换  修改后
                if (historylist.get(position).getChangeType().equals("1") || historylist.get(position).getChangeType().equals("2")) {
                    Intent intent = new Intent(context, AssetTitanDetailActivity.class);
                    intent.putExtra("id", String.valueOf(historylist.get(position).getId()));
                    intent.putExtra("name", historylist.get(position).getChangeDesc());
                    startActivity(intent);
                } else if (historylist.get(position).getChangeType().equals("21") || historylist.get(position).getChangeType().equals("22")) {
                    Intent details = new Intent(context, ExchangeDetailsActivity.class);
                    details.putExtra("id", String.valueOf(historylist.get(position).getId()));
                    details.putExtra("name", historylist.get(position).getChangeDesc());
                    startActivity(details);
                }
            }
        });
        refresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
        });
        refresh.setOnLoadMoreListener(refreshLayout -> {
            page++;
            mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_asset_titan;
    }

    @Override
    protected WalletDetailContact.WalletDetailPresent createPresent() {
        return new WalletDetailContact.WalletDetailPresent();
    }

    @OnClick({R.id.tv_titan_screen, R.id.top_up_c_btn, R.id.withdraw_c_btn, R.id.exchange_btn, R.id.tv_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titan_screen://筛选 弹框选择
                if (coin.equals("6")) {
                    showScreenDialog();//TRH筛选弹出选择
                } else if (coin.equals("5")) {
                    showBARDialog();//BAR赛选弹出选择
                } else if (coin.equals("8")) {//当参数为8时 筛选为atn钱包
                    ATNshowDialog();//筛选atn弹出框

                } else if (coin.equals("9")) {//当参数为9时 筛选按钮为dmt钱包
                    DMTshowDialog();//筛选dmt弹出框
                } else {
                    ToastUtils.showLongToast(context, "错误信息");
                }
                break;
            case R.id.top_up_c_btn://当币种为9时跳转到转出其他的跳转到充币
                if (coin.equals("8")) {
                    Intent exchanget = new Intent(context, TurnOutActivity.class);
                    exchanget.putExtra("coin", coin);
                    exchanget.putExtra("sun", sun);
                    exchanget.putExtra("title", "ATN");
                    startActivity(exchanget);
                } else {
                    Intent topUp = new Intent(context, TItanTopUpActivity.class);
                    topUp.putExtra("coin", coin);
                    startActivity(topUp);
                }
                break;
            case R.id.withdraw_c_btn://提币
                if (coin.equals("8")) {
                    Intent TTmoney1 = new Intent(context, TiTanWithdrawMoney.class);
                    TTmoney1.putExtra("id", "0");
                    TTmoney1.putExtra("coin", coin);
                    TTmoney1.putExtra("taitanSum", suntaitan);
                    TTmoney1.putExtra("title", "ATN");
                    startActivity(TTmoney1);
                } else if (coin.equals("5")) {
                    Intent TTmoney2 = new Intent(context, TiTanWithdrawMoney.class);
                    TTmoney2.putExtra("id", "0");
                    TTmoney2.putExtra("coin", coin);
                    TTmoney2.putExtra("taitanSum", suntaitan);
                    TTmoney2.putExtra("title", "BAR");
                    startActivity(TTmoney2);
                } else if (coin.equals("9")) {//币种为9 表示为dmt币种  dmt币种是没有提币的所以这显示闪兑
                    Intent exchange = new Intent(context, ExchangeActivity.class);
                    exchange.putExtra("num", "DMT");
                    exchange.putExtra("coin", coin);
                    startActivity(exchange);

                } else {
                    Intent TTmoney3 = new Intent(context, TiTanWithdrawMoney.class);
                    TTmoney3.putExtra("id", "0");
                    TTmoney3.putExtra("coin", coin);
                    TTmoney3.putExtra("taitanSum", suntaitan);
                    TTmoney3.putExtra("title", "TRH");
                    startActivity(TTmoney3);
                }
                break;
            case R.id.exchange_btn://交易页面
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("flag", 2);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_exchange://兑换
                Intent exchange = new Intent(context, ExchangeActivity.class);
                exchange.putExtra("num", tvAssetBalance.getText().toString());
                startActivity(exchange);
                break;
        }
    }

    //bra资产进入选择列表
    AlertDialog BARDialog = null;

    private void showBARDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()//默认弹窗动画
                .setCancelable(true)
                .fromRight(true)
                .setContentView(R.layout.dialog_bar)//载入布局文件
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)//设置弹窗宽高
                .setOnClickListener(R.id.all, v -> {//全部
                    type = "";
                    tvTitanScreen.setText(getResources().getString(R.string.all));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    BARDialog.dismiss();
                }).setOnClickListener(R.id.tv_purchase, v -> {
                    type = "13";
                    tvTitanScreen.setText(getResources().getString(R.string.buy));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    BARDialog.dismiss();
                }).setOnClickListener(R.id.tv_sell, v -> {//卖出
                    type = "11";
                    tvTitanScreen.setText(getResources().getString(R.string.sell));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    BARDialog.dismiss();
                }).setOnClickListener(R.id.tv_top_up, v -> {//充币
                    type = "1";
                    tvTitanScreen.setText(getResources().getString(R.string.top_up_c));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    BARDialog.dismiss();
                }).setOnClickListener(R.id.tv_exchange, v -> {//兑换
                    type = "21";
                    tvTitanScreen.setText(getResources().getString(R.string.top_exchange));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    BARDialog.dismiss();
                }).setOnClickListener(R.id.tv_service_fee, v -> {//手续费
                    type = "12";
                    tvTitanScreen.setText(getResources().getString(R.string.service_fee));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    BARDialog.dismiss();
                });
        BARDialog = builder.create();
        BARDialog.show();
    }

    //ATN 钱包筛选 (买入、卖出、手续费、提币、转出、转入)
    AlertDialog AlertATNDialog = null;

    private void ATNshowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .fromRight(true)
                .setContentView(R.layout.dialog_atn_screen)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.all, v -> {//全部
                    type = "";
                    tvTitanScreen.setText(getResources().getString(R.string.all));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_buy, v -> {//买入
                    type = "13";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_sell, v -> {//卖出
                    type = "11";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_withdraw, v -> {//提币
                    type = "2";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_turn_out, v -> {//转出
                    type = "25";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_turn_put, v -> {//转入
                    type = "15";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_service_fee, v -> {//手续费
                    type = "12";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_deal_get, v -> {//交易获得
                    type = "201";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();

                }).setOnClickListener(R.id.tv_service_get, v -> {//矿层交易获得
                    type = "202";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                }).setOnClickListener(R.id.tv_pool_get, v -> {//矿池交易获得
                    type = "203";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertATNDialog.dismiss();
                });
        AlertATNDialog = builder.create();
        AlertATNDialog.show();
    }

    //DMT 钱包筛选 (充币、卖出)
    AlertDialog AlertDMTDialog = null;

    private void DMTshowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .fromRight(true)
                .setContentView(R.layout.dialog_dmt_screen)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.all, v -> {//全部
                    type = "";
                    tvTitanScreen.setText(getResources().getString(R.string.all));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertDMTDialog.dismiss();
                }).setOnClickListener(R.id.tv_buy, v -> {//充币
                    type = "1";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertDMTDialog.dismiss();
                }).setOnClickListener(R.id.tv_sell, v -> {//卖出
                    type = "11";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertDMTDialog.dismiss();
                }).setOnClickListener(R.id.tv_sx, v -> {
                    type = "12";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertDMTDialog.dismiss();
                }).setOnClickListener(R.id.tv_sd, v -> {
                    type = "21";
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    AlertDMTDialog.dismiss();
                });
        AlertDMTDialog = builder.create();
        AlertDMTDialog.show();
    }

    //0，手续费 1，交易释放 2，充币 3，提币 4，买入 5，卖出 6，系统 7，其他 （不填写为全部）
    //泰坦资产进入选择列表
    AlertDialog screenDialog = null;

    private void showScreenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()//默认弹窗动画
                .setCancelable(true)
                .fromRight(true)
                .setContentView(R.layout.dialog_titan_screen)//载入布局文件
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)//设置弹窗宽高
                .setOnClickListener(R.id.nine, v -> {//同级交易获得
                    type = "204";
                    tvTitanScreen.setText(getResources().getString(R.string.peer_acquisition));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.all, v -> {//查询所有
                    type = "";
                    tvTitanScreen.setText(getResources().getString(R.string.all));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_buy, v -> {//买入查询
                    type = "13";
                    tvTitanScreen.setText(getResources().getString(R.string.buy));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_sell, v -> {//卖出查询
                    type = "11";
                    tvTitanScreen.setText(getResources().getString(R.string.sell));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_service_fee, v -> {//手续费查询
                    type = "12";
                    tvTitanScreen.setText(getResources().getString(R.string.service_fee));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_unfrozen, v -> {//交易释放查询
                    type = "201";
                    tvTitanScreen.setText(getResources().getString(R.string.trade_get));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_top_up, v -> {//充币查询
                    type = "1";
                    tvTitanScreen.setText(getResources().getString(R.string.top_up_c));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_withdraw, v -> {//提币查询
                    type = "2";
                    tvTitanScreen.setText(getResources().getString(R.string.withdraw_c));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_exchange, v -> {//兑换
                    type = "22";
                    tvTitanScreen.setText(getResources().getString(R.string.top_exchange));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();

                }).setOnClickListener(R.id.tv_seven, v -> {//直推交易获得
                    type = "202";
                    tvTitanScreen.setText(getResources().getString(R.string.direct_push_trade_gains));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                }).setOnClickListener(R.id.tv_eight, v -> {//等级交易加权
                    type = "203";
                    tvTitanScreen.setText(getResources().getString(R.string.rank_trading_weight));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                })
                .setOnClickListener(R.id.tv_sd, v -> {
                    type = "22";
                    tvTitanScreen.setText(getResources().getString(R.string.flash_exchange));
                    mPresent.historylist(context, coin, type, String.valueOf(page), String.valueOf(pageSize));
                    screenDialog.dismiss();
                });
        screenDialog = builder.create();
        screenDialog.show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mPresent.walletDetail(context, coin);
        refresh.autoRefresh();//自动刷新
    }

    @Override
    public void getWalletDeatilResult(Response<WalletDetailResponse> response) {
        refresh.finishRefresh();
        refresh.finishLoadMore();

        if (response.body().getCode() == Constant.SUCCESS_CODE) {

            sun = MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum());
            suntaitan = MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum());
            tvAssetBalance.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum()) + " " + getIntent().getStringExtra("type"));
            if (coin.equals("6")) {
                tvTixianBalance.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum()));//提现余额
            } else if (coin.equals("5")) {
                tvTixianBalance.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getFrozennum()));//提现余额
            } else {
                tvTixianBalance.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getCoinnum()));//可提现余额
            }
            tvTitanTradingFrozen.setText(MoneyUtil.priceFormatDoubleFour(response.body().getData().getWellet().getFrozennum()));//交易冻结
        }

    }


    List<HistoryListResponse.mData> data;

    @Override
    public void gethistorylist(Response<HistoryListResponse> response) {
        refresh.finishRefresh();
        refresh.finishLoadMore();
        if (response.body().getCode() == Constant.SUCCESS_CODE) {
            if (page == 1) {
                historylist.clear();
            }
            data = response.body().getData();
            if (!ObjectUtils.isEmpty(data)) {
                rvNormalShow.setVisibility(View.GONE);
                historylist.clear();
                historylist.addAll(data);
                mAdapter.notifyDataSetChanged();
                RecyclerViewAnimation.runLayoutAnimation(rv);//动画显示
                if (data != null && data.size() >= pageSize) {
                    refresh.setEnableLoadMore(true);
                } else {
                    refresh.setEnableLoadMore(false);
                }
                rv.setVisibility(View.VISIBLE);
            } else if (page != 1) {
                refresh.setEnableLoadMore(false);
            } else {
                rvNormalShow.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, getResources().getString(R.string.network_error));
    }


}
