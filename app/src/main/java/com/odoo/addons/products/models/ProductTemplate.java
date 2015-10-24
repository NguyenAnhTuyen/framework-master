package com.odoo.addons.products.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

/**
 * Created by AnhTuyen on 24/10/2015.
 */
public class ProductTemplate extends OModel {
    public static final String TAG = ProductTemplate.class.getSimpleName();
    public static final String AUTHORITY = "com.odoo.addons.products.product_products";
    OColumn name= new OColumn("Name", OVarchar.class);
    OColumn product_variant_ids = new OColumn("Product" ,ProductProduct.class,OColumn.RelationType.OneToMany);

    public ProductTemplate(Context context, OUser user) {
        super(context, "product.template", user);
    }
    @Override
    public Uri uri(){
        return buildURI(AUTHORITY);
    }
}
