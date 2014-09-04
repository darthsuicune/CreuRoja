require 'rails_helper'

describe "vehicles/index" do
	before(:each) do
		assign(:vehicles, [FactoryGirl.create(:vehicle), FactoryGirl.create(:vehicle)])
	end

	it "renders a list of vehicles" do
		render
		# Run the generator again with the --webrat flag if you want to use webrat matchers
		expect(rendered).to match "Indicative"
		expect(rendered).to match "Brand"
		expect(rendered).to match "Model"
		expect(rendered).to match "License"
		expect(rendered).to match "MyString"
		expect(rendered).to match "Notes"
	end
end
