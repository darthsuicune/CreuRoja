require 'rails_helper.rb'

describe "locations/map" do
	it "works?" do
		render 
		
		expect(rendered).to match(/Map/)
		expect(rendered).to match(/<div id="map"/)
	end
end