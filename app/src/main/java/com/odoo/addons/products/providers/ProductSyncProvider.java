package com.odoo.addons.products.providers;

import com.odoo.addons.products.models.ProductProduct;
import com.odoo.addons.products.models.ProductTemplate;
import com.odoo.core.orm.provider.BaseModelProvider;

public class ProductSyncProvider extends BaseModelProvider {
    public static final String TAG = ProductSyncProvider.class.getSimpleName();

    @Override
    public String authority() {
        return ProductTemplate.AUTHORITY;
    }
}
