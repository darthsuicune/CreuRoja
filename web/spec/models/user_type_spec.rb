require 'rails_helper'

describe UserType do
  let(:user_type) { FactoryGirl.create(:user_type) }
  subject { user_type }
  
  it { should respond_to(:user) }
  it { should respond_to(:user_id) }
  it { should respond_to(:user_type) }

	describe "types" do
		let(:user) { FactoryGirl.create(:user) }
		describe "assign type" do
			it "should create a user_type" do
				expect { user.user_types.build(user_type: "asdf")
							user.save }.to change(UserType, :count).by(1)
			end
		end

		describe "to user that already has it" do
			it "should fail" do
				expect { user.user_types.build(user_type: "asdf")
							user.user_types.build(user_type: "asdfasdf")
							user.save
							user.user_types.build(user_type: "asdf")
							user.save }.to raise_error
			end
		end
	end
end
