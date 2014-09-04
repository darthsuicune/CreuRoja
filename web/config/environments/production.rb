CreuRoja::Application.configure do
	# Settings specified here will take precedence over those in config/application.rb.

	#Make the assets work as per http://railsblog.kieser.net/2013/08/rails4-phusion-passenger-asset-pipeline.html
	config.assets.digest = true
	config.assets.compile = true
	
	config.eager_load = true
	
	#Make rails serve the static assets as well
	config.serve_static_assets = true
	
	config.action_mailer.smtp_settings = { enable_starttls_auto: false, openssl_verify_mode: "none" }
end
