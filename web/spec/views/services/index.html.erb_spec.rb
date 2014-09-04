require 'rails_helper'

describe "services/index" do
	let(:user) { FactoryGirl.create(:user) }
	let(:assembly) { FactoryGirl.create(:location) }
	before(:each) do
		sign_in user
		@service1 = FactoryGirl.create(:service)
		@service2 = FactoryGirl.create(:service)
		assign(:services, [@service1, @service2])
		@service1.assembly_id = assembly.id
		@service2.assembly_id = assembly.id
	end

	it "renders a list of services" do
		render
		# Run the generator again with the --webrat flag if you want to use webrat matchers
		expect(rendered).to match "Name"
		expect(rendered).to match "Description"
		expect(rendered).to match assembly.name
		expect(rendered).to match "Code"
	end
end
