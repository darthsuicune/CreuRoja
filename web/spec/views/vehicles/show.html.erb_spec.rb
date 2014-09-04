require 'rails_helper'

describe "vehicles/show" do
	let(:user) { FactoryGirl.create(:admin) }
	before(:each) do
		sign_in user
		@vehicle = FactoryGirl.create(:vehicle)
	end

	it "renders attributes in <p>" do
		render
		# Run the generator again with the --webrat flag if you want to use webrat matchers
		expect(rendered).to match(/Brand/)
		expect(rendered).to match(/Model/)
		expect(rendered).to match(/License/)
		expect(rendered).to match(/Indicative/)
		expect(rendered).to match(/MyString/)
		expect(rendered).to match(/1/)
		expect(rendered).to match(/Notes/)
	end
end
