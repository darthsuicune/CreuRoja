require 'rails_helper'

describe LocationUsersController do
	let(:user) { FactoryGirl.create(:user) }
	let(:location) { FactoryGirl.create(:location, location_type => "assembly") }
	let(:location_user) { FactoryGirl.create(:location_user, user_id => user.id, location_id => location.id) }
	describe "POST create" do
	end
	describe "PUT update" do
	end
	describe "DELETE destroy" do
	end
end
