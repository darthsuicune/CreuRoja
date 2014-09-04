require 'rails_helper'

# Specs in this file have access to a helper object that includes
# the SessionsHelper. For example:
#
# describe SessionsHelper do
#   describe "string concat" do
#     it "concats two strings with spaces" do
#       expect(helper.concat_strings("this","that")).to eq("this that")
#     end
#   end
# end
describe SessionsHelper do
	let(:user) { FactoryGirl.create(:user) }
	describe "sign_in(user)" do
	end
	
	describe "signed_in?" do
	end
	
	describe "sign_out" do
		before { sign_in user }
		
		it "should log out the user" do
			expect {
				sign_out
			}.to change(Session, :count).by(-1)
		end
		
		it "should put current_user to nil" do
			expect(@current_user).not_to be_nil
			sign_out
			expect(@current_user).to be_nil
		end
	end
	
	describe "current_user" do
	end
	
	describe "current_user?(user)" do
	end
	
	describe "redirect_back_or(default)" do
	end
	
	describe "store_location" do
	end
	
	describe "signed_in_user" do
	end
	
end
