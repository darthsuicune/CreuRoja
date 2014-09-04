require 'rails_helper'

describe "vehicles/new" do
	let(:user) { FactoryGirl.create(:user) }
	
	before(:each) do
		sign_in user
		@vehicle = Vehicle.new
	end

	it "renders new vehicle form" do
		render

		# Run the generator again with the --webrat flag if you want to use webrat matchers
		assert_select "form[action=?][method=?]", vehicles_path, "post" do
			assert_select "input#vehicle_brand[name=?]", "vehicle[brand]"
			assert_select "input#vehicle_model[name=?]", "vehicle[model]"
			assert_select "input#vehicle_license[name=?]", "vehicle[license]"
			assert_select "input#vehicle_indicative[name=?]", "vehicle[indicative]"
			assert_select "select#vehicle_vehicle_type[name=?]", "vehicle[vehicle_type]"
			assert_select "input#vehicle_places[name=?]", "vehicle[places]"
			assert_select "input#vehicle_notes[name=?]", "vehicle[notes]"
			assert_select "input#vehicle_operative[name=?]", "vehicle[operative]"
		end
	end
end
