T5.extendInitializers({
	ajaxUpload:function (spec) {
		// find tapestry form
		var tapestryForm = $(spec.formId);
		// bind on submit form event
		tapestryForm.observe(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function () {
			// stop observing submit event to prevent usual tapestry form submission
			tapestryForm.stopObserving(Tapestry.FORM_PROCESS_SUBMIT_EVENT);
			// connect to tapestry form submission event
			tapestryForm.observe(Tapestry.FORM_PROCESS_SUBMIT_EVENT, function () {
				// clear submit event listener to prevent infinite loop
				tapestryForm.onsubmit = null;
				// upload files in iframe using jquery plugin
				jQuery("#" + spec.formId).upload().promise().done(function (response) {
					// update zone with POST result
					var zone = Tapestry.findZoneManager(spec.formId);
					zone.processReply(response);
				});
			});
		});
	}
});