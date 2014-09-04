require 'rails_helper'

RSpec.describe "Issues", :type => :request do
	let(:user) { FactoryGirl.create(:admin) }
	before { sign_in user }
	describe "GET /issues" do
		it "works! (now write some real specs)" do
			get issues_path
			expect(response.status).to eq(200)
		end
	end
end
