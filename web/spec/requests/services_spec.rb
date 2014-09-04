require 'rails_helper'

describe "Services" do
	describe "without signin in" do
		describe "GET /services" do
			it "redirects to login" do
				get services_path
				expect(response.status).to eq(302)
			end
		end
	end
	describe "signed in" do
		let(:user) { FactoryGirl.create(:admin) }
		before { sign_in user }
		describe "GET /services" do
			it "shows the services index" do
				# Run the generator again with the --webrat flag if you want to use webrat methods/matchers
				get services_path
				expect(response.status).to eq(200)
			end
		end
	end
end