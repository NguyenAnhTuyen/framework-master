package com.odoo.addons.products.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.products.models.ProductProduct;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class ProductSyncService extends OSyncService {

    public static final String TAG= ProductSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(getApplicationContext(), ProductProduct.class,service,true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }

}
