require 'rails_helper'

RSpec.describe ServiceUsersController, :type => :controller do
	let(:service_user) { FactoryGirl.create(:service_user) }
	describe "without signing in" do
		describe "POST create" do
			it "returns http success" do
				post :create
				expect(response.status).to eq(302)
			end
		end

		describe "PUT update" do
			it "returns http success" do
				put :update, {id: 0, service_user: {}}
				expect(response.status).to eq(302)
			end
		end

		describe "DELETE destroy" do
			it "returns http success" do
				delete :destroy, {id: 0}
				expect(response.status).to eq(302)
			end
		end
	end
end
