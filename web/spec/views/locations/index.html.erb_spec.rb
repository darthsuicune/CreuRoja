require 'rails_helper'

describe "locations/index" do
	let(:user) { FactoryGirl.create(:user) }
	before(:each) do
		sign_in user
		assign(:locations, [
			FactoryGirl.create(:location),
			FactoryGirl.create(:location, latitude: 2.5)
		])
	end

	it "renders a list of locations" do
		render
		# Run the generator again with the --webrat flag if you want to use webrat matchers
		expect(rendered).to match "Name"
		expect(rendered).not_to match "Description"
		expect(rendered).to match "Address"
		expect(rendered).to match "Phone"
		expect(rendered).not_to match "1.5"
		expect(rendered).not_to match "2.5"
		expect(rendered).to match "MyType"
	end
end

