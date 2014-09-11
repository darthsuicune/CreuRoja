require 'rails_helper'

describe "locations/index" do
	let(:user) { FactoryGirl.create(:user) }
	let(:location) { FactoryGirl.create(:location) }
	let(:locations) { [location] }
	
	before { 
		sign_in user 
		assign(:locations, [location])
	}
	
	it "renders the correct json" do
		render template: "locations/index", formats: :json
		
		expect(rendered).to eq(locations.to_json except: [:created_at, :expiredate])
	end
end