require 'rails_helper'

RSpec.describe "vehicle_positions/index.html.erb", :type => :view do
	it "works?" do
		render 
		
		expect(rendered).to match(/Map/)
		expect(rendered).to match(/<div id="map"/)
	end
end
