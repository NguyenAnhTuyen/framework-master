package com.odoo.addons.products.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.App;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OBlob;
import com.odoo.core.orm.fields.types.OBoolean;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OTimestamp;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;


public class ProductProduct extends OModel{
    public static final String TAG = ProductProduct.class.getSimpleName();


    OColumn name = new OColumn("Name", OVarchar.class).setSize(32);
    OColumn create_date = new OColumn("Date Create", ODateTime.class);
    OColumn active = new OColumn("Stock OK", OBoolean.class).setDefaultValue(false);
    OColumn image_small = new OColumn("Images", OBlob.class).setDefaultValue("false");
    OColumn price = new OColumn("Price", OFloat.class);
    OColumn purchase_count = new OColumn("Purchases", OInteger.class);
    OColumn qty_available = new OColumn("Quantity On Hand",OFloat.class);
//    OColumn product_tmpl_id = new OColumn("Product Template", ProductTemplate.class,OColumn.RelationType.ManyToOne);
    public ProductProduct(Context context, OUser user) {
        super(context, "product.product", user);
        setDefaultNameColumn("name");
    }


}
