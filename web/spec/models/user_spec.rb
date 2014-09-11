require 'rails_helper'

describe User do
	let(:pass) { "foobar" }
	before { @user = User.new( name: "Example", surname: "Example", email: "user@example.com", 
										role: "volunteer", password: pass, password_confirmation: pass ) }
	subject { @user }

	it { should respond_to(:name) }
	it { should respond_to(:surname) }
	it { should respond_to(:email) }
	it { should respond_to(:role) }
	it { should respond_to(:language) }
	it { should respond_to(:password_digest) }
	it { should respond_to(:password) }
	it { should respond_to(:password_confirmation) }
	it { should respond_to(:authenticate) }
	it { should respond_to(:active) }
	it { should respond_to(:allowed_to?) }
	it { should respond_to(:sessions) }
	it { should respond_to(:user_types) }
	it { should respond_to(:assemblies) }
	it { should respond_to(:resettoken) }
	it { should respond_to(:resettime) }

	it { should be_valid }

	describe "name" do
		describe "is not present" do
			before { @user.name = " " }
			it { should_not be_valid }
		end
		describe "is waaaay too long" do
			before { @user.name = "abcdefghijklmnopqrstuvwxyz" * 4 }
			it { should_not be_valid }
		end
	end
	describe "surname" do
		describe "is not present" do
			before { @user.surname = " " }
			it { should_not be_valid }
		end
		describe "is waaaay too long" do
			before { @user.surname = "abcdefghijklmnopqrstuvwxyz" * 4 }
			it { should_not be_valid }
		end
	end
	describe "email" do
		describe "is not present" do
			before { @user.email = " " }
			it { should_not be_valid }
		end
		describe "format is invalid" do
			it "should be invalid" do
				addresses = %w[user@foo,com user_at_foo.org example.user@foo. foo@bar_baz.com foo@bar+baz.com]
				addresses.each do |invalid_address|
					@user.email = invalid_address
					expect(@user).not_to be_valid
				end
			end
		end
		describe "format is valid" do
			it "should be valid" do
				addresses = %w[user@foo.COM A_US-ER@f.b.org frst.lst@foo.jp a+b@baz.cn]
				addresses.each do |valid_address|
					@user.email = valid_address
					expect(@user).to be_valid
				end
			end
		end
		describe "is already taken" do
			before do
				user2 = @user.dup
				user2.email = @user.email.upcase
				user2.save
			end
			it { should_not be_valid }
		end
		describe "is not downcased" do
			let(:email1) { "dEnIs@localhost.com" }
			it "should be lower case" do
				@user.email = email1
				@user.save
				expect(@user.reload.email).to eq(email1.downcase)
			end
		end
	end
	describe "password" do
		describe "is not present" do
			before { @user.password = @user.password_confirmation = " " }
			it { should_not be_valid }
		end
		describe "is too short" do
			before { @user.password = @user.password_confirmation = "a" * 5 
			         @user.save }
			it { should_not be_valid }
		end
		describe "isn't matching confirmation" do
			before { @user.password_confirmation = "mismatch" }
			it { should_not be_valid }
		end
		describe "confirmation is nil" do
			before { @user.password = "asdfgh"
			         @user.password_confirmation = nil }
			it { should_not be_valid }
		end
	end
	describe "return value of authenticate method" do
		before { @user.save }
		let(:found_user) { User.find_by_email(@user.email) }
		describe "with valid password" do
			it { should == found_user.authenticate(@user.password) }
		end
		describe "with invalid password" do
			let(:user_for_invalid_password) { found_user.authenticate("invalid") }
			
			it { should_not eq(user_for_invalid_password) }
			it "should be false" do
				expect(user_for_invalid_password).to be_falsey
			end
		end
	end
	describe "permission system" do
		describe "admins" do
			before { @user.role = "admin"
						@user.save }
			it { should be_allowed_to(:manage_admin_users) }
			it { should be_allowed_to(:see_own_profile) }
		end
		describe "technicians" do
			before { @user.role = "technician"
						@user.save }
			it { should be_allowed_to(:see_own_profile) }
			it { should be_allowed_to(:manage_users) }
			it { should_not be_allowed_to(:manage_admin_users) }
		end
		describe "volunteers" do
			before { @user.role = "volunteer"
						@user.save }
			it { should be_allowed_to(:see_own_profile) }
			it { should_not be_allowed_to(:manage_technician_users) }
			it { should_not be_allowed_to(:manage_admin_users) }
		end
		describe "a blocked user" do
			before { @user.active = false
						@user.save }
			it { should_not be_allowed_to(:see_own_profile) }
		end
	end
	
	describe "get_visible_locations" do
	end
	
	describe "create_reset_password_token" do
		let(:user) { FactoryGirl.create(:user) }
		before { user.create_reset_password_token }
		it "creates a token" do
			expect {
				user.create_reset_password_token
			}.to change(user, :resettoken)
		end
		it "sets the resettime to now" do
			expect(user.resettime).to be_within(5.seconds).of(Time.now)
		end
		it "can create tokens that last further" do
			expect {
				user.create_reset_password_token(1.year.from_now)
			}
		end
	end
	
	describe "reset_password(password)" do
		let(:new_password) { "fdsa"*2 }
		it "resets the password" do
			expect {
				@user.reset_password new_password
			}.to change(@user, :password_digest)
		end
		it "authenticates with the new one" do
			@user.reset_password new_password
			result = @user.authenticate new_password
			expect(result).to be @user
		end
		it "doesn't authenticate with the old one" do
			@user.reset_password new_password
			result = @user.authenticate pass
			expect(result).to be_falsey
		end
	end
	
	describe "create_session_token" do
		let(:user) { FactoryGirl.create(:user) }
		it "creates a session" do
			expect {
				user.create_session_token
			}.to change(Session, :count).by(1)
		end
	end
	
	
end
