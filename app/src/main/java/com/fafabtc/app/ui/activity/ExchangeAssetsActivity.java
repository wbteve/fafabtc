package com.fafabtc.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ActivityExchangeAssetsBinding;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.BalanceAssetsFragment;
import com.fafabtc.app.ui.fragment.BlockchainAssetsFragment;
import com.fafabtc.app.ui.fragment.ErrorFragment;
import com.fafabtc.app.vm.ExchangeAssetsViewModel;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

@Injectable
@BindLayout(R.layout.activity_exchange_assets)
public class ExchangeAssetsActivity extends BaseActivity<ActivityExchangeAssetsBinding> {

    private static final String EXTRA_ACCOUNT_ASSETS = "ExchangeAssetsActivity.EXTRA_ACCOUNT_ASSETS";
    private static final String EXTRA_EXCHANGE = "ExchangeAssetsActivity.EXTRA_EXCHANGE";

    private static int[] ASSETS_NAMES = {R.string.balance_assets, R.string.blockchain_assets};

    private AccountAssets accountAssets;
    private Exchange exchange;

    private ExchangeAssetsViewModel viewModel;
    private AssetsPagerAdapter pagerAdapter;

    public static void start(Context context, AccountAssets accountAssets, Exchange exchange) {
        Intent starter = new Intent(context, ExchangeAssetsActivity.class);
        starter.putExtra(EXTRA_ACCOUNT_ASSETS, accountAssets);
        starter.putExtra(EXTRA_EXCHANGE, exchange);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            accountAssets = intent.getParcelableExtra(EXTRA_ACCOUNT_ASSETS);
            exchange = intent.getParcelableExtra(EXTRA_EXCHANGE);
            setTitle(getString(R.string.exchange_assets_title_format, accountAssets.getName(), exchange.getName().toUpperCase()));
        }

        pagerAdapter = new AssetsPagerAdapter(getSupportFragmentManager());
        binding.pagerExchageAssets.setAdapter(pagerAdapter);

        viewModel = getViewModel(ExchangeAssetsViewModel.class);
        viewModel.setAccountAssets(accountAssets);
        viewModel.setExchange(exchange);
//        viewModel.updateExchangeAssets();
    }

    private class AssetsPagerAdapter extends FragmentPagerAdapter {

        public AssetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BalanceAssetsFragment.newInstance(accountAssets, exchange);
                case 1:
                    return BlockchainAssetsFragment.newInstance(accountAssets, exchange);
                default:
                    return ErrorFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return ASSETS_NAMES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(ASSETS_NAMES[position]);
        }
    }

}
