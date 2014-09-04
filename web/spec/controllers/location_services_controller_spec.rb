require 'rails_helper'

RSpec.describe LocationServicesController, :type => :controller do
	let(:location_service) { FactoryGirl.create(:location_service) }
	describe "without signin" do
		describe "POST create" do
			it "redirects to root" do
				post :create
				expect(response.status).to eq(302)
			end
		end

		describe "PUT update" do
			it "redirects to root" do
				put :update, {id: 0, location_service: {}}
				expect(response.status).to eq(302)
			end
		end

		describe "DELETE destroy" do
			it "redirects to root" do
				delete :destroy, {id: 0}
				expect(response.status).to eq(302)
			end
		end
	end
end
