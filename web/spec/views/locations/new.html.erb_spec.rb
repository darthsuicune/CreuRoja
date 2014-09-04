require 'rails_helper'

describe "locations/new" do
	let(:user) { FactoryGirl.create(:user) }
	before(:each) do
		sign_in user
		@location = Location.new
	end

	it "renders new location form" do
		render

		# Run the generator again with the --webrat flag if you want to use webrat matchers
		assert_select "form[action=?][method=?]", locations_path, "post" do
			assert_select "input#location_name[name=?]", "location[name]"
			assert_select "input#location_description[name=?]", "location[description]"
			assert_select "input#location_address[name=?]", "location[address]"
			assert_select "input#location_phone[name=?]", "location[phone]"
			assert_select "input#location_latitude[name=?]", "location[latitude]"
			assert_select "input#location_longitude[name=?]", "location[longitude]"
			assert_select "select#location_location_type[name=?]", "location[location_type]"
			assert_select "input#location_active[name=?]", "location[active]"
		end
	end
end
