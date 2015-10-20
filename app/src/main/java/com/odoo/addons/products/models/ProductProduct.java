package com.odoo.addons.products.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.App;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OBoolean;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OTimestamp;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

/**
 * Created by AnhTuyen on 14/10/2015.
 */
public class ProductProduct extends OModel{
    public static final String TAG = ProductProduct.class.getSimpleName();
    public static final String AUTHORITY = App.APPLICATION_ID +
            ".core.provider.content.sync.product_product";
    OColumn name_template = new OColumn("Name", OVarchar.class).setSize(0);
//    OColumn create_date = new OColumn("Date Create", OTimestamp.class).setSize(6);
//    OColumn active = new OColumn("Stock OK", OBoolean.class).setDefaultValue(false);

    public ProductProduct(Context context, OUser user) {
        super(context, "product.product", user);
        setDefaultNameColumn("name_template");
    }
    @Override
    public Uri uri(){
        return buildURI(AUTHORITY);
    }
}
