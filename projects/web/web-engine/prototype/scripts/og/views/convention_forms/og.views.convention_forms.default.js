/*
 * Copyright (C) 2018 - present McLeod Moores Software Limited.  All rights reserved.
 */
$.register_module({
	name: 'og.views.convention_forms.default',
	dependencies: [
		'og.api.rest',
		'og.common.util.ui'
	],
	obj: function () {
		var module = this, Form = og.common.util.ui.Form, api = og.api.rest, constructor;
		constructor = function (config) {
			var load_handler = config.handler || $.noop, 
				selector = config.selector, 
				master = config.data.template_data.configXML,
				convention_type = config.type,
				loading = config.loading || $.noop,
				deleted = config.data.template_data.deleted,
				is_new = config.is_new,
				orig_name = config.data.template_data.name,
				new_name = '',
				resource_id = config.data.template_data.object_id,
				save_new_handler = config.save_new_handler,
				save_handler = config.save_handler,
				editor,
				form = new Form({
					module: 'og.views.forms.conventions_default_tash',
					data: { name: null },
					selector: selector,
					extras: { name: orig_name, raw: is_new ? '<xml />' : master},
					processor: function (data) { new_name = data.name }
				}),
				form_id = '#' + form.id;
			var save_resource = function (result) {
				var as_new = result.extras.as_new;
				if (!deleted && !is_new && as_new && (orig_name === new_name))
					return window.alert('Please select a new name.');
				api.conventions.put({
					id: as_new ? void 0 : resource_id,
							name: new_name, xml: editor.getSession().getValue(), type: convention_type, loading: loading
				}).pipe(as_new ? save_new_handler : save_handler);
			};
			og.dev.warn('using default convention template for convention type:\n' + convention_type);
			form.on('form:load', function () {
				var textarea, id = og.common.id(), header = '\
					<header class="OG-header-generic">\
					<div class="OG-tools"></div><h1 class="og-js-name">' + orig_name + '</h1>\
					</header>';
				$('.OG-layout-admin-details-center .ui-layout-header').html(header);
				setTimeout(load_handler.partial(form));
				$(selector).css({'overflow': 'hidden'}).find('.OG-convention').addClass('og-expand');
				textarea = $('textarea[name=raw]').addClass(id).hide();
				editor = ace.edit('og-js-editor');
				editor.getSession().setMode('ace/mode/xml');
				editor.getSession().setValue(textarea.val());
				og.common.gadgets.manager.register({
					alive: function () {
						return !!$('.' + id).length;
					},
					resize: function () {
						editor.resize();
					}
				});
			}).on('keyup', form_id + ' [name=name]', function (event) {
				$('.OG-layout-admin-details-center .og-js-name').text($(event.target).val());
			}).on('form:submit', save_resource).dom();
		};
		constructor.is_default = true;
		return constructor;
	}
});