/*
 * Copyright (C) 2018 - present McLeod Moores Software Limited.  All rights reserved.
 */
$.register_module({
	name: 'og.views.convention_forms.compoundingiborlegconvention',
	dependencies: [
		'og.api.rest',
		'og.common.util.ui'
	],
	obj: function () {
		var ui = og.common.util.ui, 
			forms = og.views.forms, 
			api = og.api.rest, 
			Form = ui.Form,
			ATTR = 'attributes',
			EIDS = 'externalIdBundle',
			INDX = '<INDEX>',
			EMPT = '<EMPTY>',
			type_map = [
				[['0', INDX].join('.'),								Form.type.STR],
				['name', 											Form.type.STR],
				['iborIndexConvention',		 						Form.type.STR],
				['underlyingConventionName', 						Form.type.STR],
				['paymentTenor',									Form.type.STR],
				['compoundingType', 								Form.type.STR],
				['compositionTenor', 								Form.type.STR],
				['stubTypeCompound', 								Form.type.STR],
				['isExchangeNotional', 								Form.type.BOO],
				['settlementDays', 									Form.type.STR],
				['isEOM',			 								Form.type.BOO],
				['stubTypeLeg', 									Form.type.STR],
				['paymentLag', 										Form.type.STR],
				['uniqueId',										Form.type.STR],
				[[EIDS, 'ID', INDX, 'Scheme'].join('.'),	 		Form.type.STR],
				[[EIDS, 'ID', INDX, 'Value'].join('.'),				Form.type.STR],
				[['id', EMPT, 'scheme'].join('.'),					Form.type.STR],
				[['id', EMPT, 'value'].join('.'),					Form.type.STR],
				[[ATTR, EMPT].join('.'),							Form.type.STR], 
				[[ATTR, INDX, 'Key'].join('.'),						Form.type.STR], 
				[[ATTR, INDX, 'Value'].join('.'),					Form.type.STR], 
				].reduce(function (acc, val) { return acc[val[0]] = val[1], acc; }, {});
        var arr = function (obj) { return arr && $.isArray(obj) ? obj : typeof obj !== 'undefined' ? [obj] : [] };
        var constructor = function (config) {
        	var load_handler = config.handler || $.noop,
	        	selector = config.selector,
	        	loading = config.loading || $.noop,
	        	deleted = config.data.template_data.deleted,
	        	is_new = config.is_new,
	        	orig_name = config.data.template_data.name,
	        	resource_id = config.data.template_data.object_id,
	        	save_new_handler = config.save_new_handler, 
	        	save_handler = config.save_handler,
	        	master = config.data.template_data.configJSON.data,
	        	convention_type = config.type,
	        	underlyingConventionName = master.underlyingConventionName,
	        	isExchangeNotional = master.isExchangeNotional,
	        	isEOM = master.isEOM,
	        	form = new Form({
	        		module: 'og.views.forms.compounding-ibor-leg-convention_tash',
	        		data: master,
	        		type_map: type_map,
	        		selector: selector,
	        		extras: {
	        			name: master.name,
	        		},
	        		processor: function (data) {
	        			data.id = data.id.filter(function (v) { return v !== void 0; });
            			data[EIDS] = arr(data[EIDS]).filter(function (v) { return v !== void 0; });
            			data[ATTR] = arr(data[ATTR]).filter(function (v) { return v !== void 0; });
	        		}
	        	}),
	        	form_id = '#' + form.id,
        		save_resource = function (result) {
        			var data = result.data,
        				meta = result.meta,
        				as_new = result.extras.as_new;
        			data.isEOM = isEOM;
        			data.isExchangeNotional = isExchangeNotional;
        			if (as_new && (orig_name == data.name)) { return window.alert('Please select a new name.') };
        			if (!data.externalIdBundle.ID.length) { return window.alert('Please add at least one external identifier') }; 
        			api.conventions.put({
        				id: as_new ? void 0 : resource_id,
        				name: data.name,
        				json: JSON.stringify({ data: data, meta: meta }),
        				type: convention_type,
        				loading: loading,
        				handler: as_new ? save_new_handler : save_handler
        			});
        		},
        		load_resource = function () {
            		var header = '\
            			<header class="OG-header-generic">\
            			<div class="OG-tools"></div>\
            			<h1>\
            			<span class="og-js-name">' + master.name + '</span>\
            			</h1>\
            			  &nbsp(Compounding *IBOR Swap Leg Convention)\
            			</header>\
            			';
            		$('.OG-layout-admin-details-center .ui-layout-header').html(header);
            		$(form_id + ' input[name=isEOM').prop('checked', isEOM);
            		$(form_id + ' input[name=isExchangeNotional').prop('checked', isExchangeNotional);
            		$(form_id + ' input[name=settlementDays').val(master.settlementDays.toString());
            		$(form_id + ' input[name=paymentLag').val(master.paymentLag.toString());
            		$(form_id + ' p[id=warning]')[0].hidden = ($('#' + form.children[0].id)[0].options.length > 1);
            		setTimeout(load_handler.partial(form));
                };
            form.on('form:submit', save_resource)
            	.on('form:load', load_resource)
            	.on('click', form_id + ' input[name=isEOM]', function (event) { isEOM = !isEOM })
            	.on('click', form_id + ' input[name=isExchangeNotional]', function (event) { isExchangeNotional = !isExchangeNotional });
            form.children = [
            	new ui.Dropdown({
            		form: form,
            		placeholder: 'Please select...',
            		value: !underlyingConventionName ? "" : underlyingConventionName,
            		resource: 'conventions.conventionIds',
            		data_generator: function (handler) {
            			api.conventions.convention_ids.get({ conventionType: 'IborIndex'}).pipe(function (result) {
            				handler(result.data.map(function (convention) {
            					var split = convention.split('|');
            					return { value: split[0], text: split[0], selected: split[0] === underlyingConventionName };
            				}))
            			})
            		},
            		index: 'iborIndexConvention'
            	}),
            	new ui.Dropdown({
            		form: form,
            		placeholder: 'Please select...',
            		value: master.paymentTenor ? master.paymentTenor : "",
            		resource: 'conventionutils.tenor',
            		data_generator: function (handler) {
            			api.conventions.convention_utils.tenor.get().pipe(function (result) {
            				handler(result.data.map(function (tenor) {
            					return { value: tenor, text: tenor };
            				}))
            			})
            		},
            		index: 'paymentTenor'
            	}),
            	new ui.Dropdown({
            		form: form,
            		placeholder: 'Please select...',
            		value: master.compositionTenor ? master.compositionTenor : "",
            		resource: 'conventionutils.tenor',
            		data_generator: function (handler) {
            			api.conventions.convention_utils.tenor.get().pipe(function (result) {
            				handler(result.data.map(function (tenor) {
            					return { value: tenor, text: tenor };
            				}))
            			})
            		},
            		index: 'compositionTenor'
            	}),
            	new ui.Dropdown({
            		form: form,
            		placeholder: 'Please select...',
            		value: master.compoundingType[1] ? master.compoundingType[1] : "",
            		resource: 'conventionutils.compoundingtype',
            		data_generator: function (handler) {
            			api.conventions.convention_utils.compoundingtype.get().pipe(function (result) {
            				handler(result.data.map(function (stub_type) {
            					var split = stub_type.split('|');
            					return { value: split[0], text: split[1] };
            				}))
            			})
            		},
            		index: 'compoundingType'
            	}),
            	new ui.Dropdown({
            		form: form,
            		placeholder: 'Please select...',
            		value: master.stubTypeCompound[1] ? master.stubTypeCompound[1] : "",
            		resource: 'conventionutils.stubtype',
            		data_generator: function (handler) {
            			api.conventions.convention_utils.stubtype.get().pipe(function (result) {
            				handler(result.data.map(function (stub_type) {
            					var split = stub_type.split('|');
            					return { value: split[0], text: split[1] };
            				}))
            			})
            		},
            		index: 'stubTypeCompound'
            	}),
            	new ui.Dropdown({
            		form: form,
            		placeholder: 'Please select...',
            		value: master.stubTypeLeg[1] ? master.stubTypeLeg[1] : "",
            		resource: 'conventionutils.stubtype',
            		data_generator: function (handler) {
            			api.conventions.convention_utils.stubtype.get().pipe(function (result) {
            				handler(result.data.map(function (stub_type) {
            					var split = stub_type.split('|');
            					return { value: split[0], text: split[1] };
            				}))
            			})
            		},
            		index: 'stubTypeLeg'
            	}),
            	new og.views.convention_forms.ExternalIdBundle({
            		form: form,
            		data: master.externalIdBundle,
            		index: 'externalIdBundle',
            	}),
            	new og.views.convention_forms.Attributes({
            		form: form,
            		attributes: master.attributes,
            		index: 'attributes'
            	})
            ];
            form.dom();
        };
        constructor.type_map = type_map;
        return constructor;
	}
})