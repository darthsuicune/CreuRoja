require 'rails_helper'

describe "Users" do
	subject { response }

	describe "Http requests" do
		let(:another_user) { FactoryGirl.create(:user) }
		before { @user = FactoryGirl.create(:user) }
		describe "without signing in" do
			describe "GET /users" do
				before { get users_path }
				it { should redirect_to(signin_url) }
			end
			describe "individual user" do
				describe "GET /users/:id" do
					before { get user_path( { :id => 1 } ) }
					it { should redirect_to(signin_url) }
				end
				describe "non existing user" do
					before { get user_path( { :id => 5 } ) }
					it { should redirect_to(signin_url) }
				end
			end
			describe "create user" do
				before { get new_user_path }
				it { should redirect_to(signin_url) }
			end
		end
		describe "signed in" do
			before { sign_in @user }
			subject { page }
			describe "navigation menu" do
				before { visit users_path }
				it { should_not have_content(I18n.t(:user_list_title)) }
				it { should have_content(I18n.t(:logout)) }
			end
			describe "individual user" do
				describe "other user profile" do
					before { get user_path( { :id => another_user.id } ) }
					it { should_not have_content(another_user.name) }
					it { should_not have_content(another_user.surname) }
					it { should_not have_content(another_user.email) }
					it { should redirect_to(root_url) }
				end
				describe "same profile" do
					before { get user_path( { :id => @user.id } ) }
					it "should not redirect to root" do
						expect(response.status).not_to eq(302)
						expect(response.status).to eq(200)
					end
					it { should have_content(@user.name) }
					it { should have_content(@user.surname) }
					it { should have_content(@user.email) }
				end
				describe "non existing user" do
					it "should raise a RecordNotFound error" do
						expect { 
							get user_path( { :id => 555 } ) 
						}.to raise_error(ActiveRecord::RecordNotFound)
					end
				end
			end
			describe "create user" do
				before { get new_user_path }
			end
		end
	end
  
	describe "Json Requests" do
		let(:user) { FactoryGirl.create(:user) }
		let(:admin) { FactoryGirl.create(:admin) }
		describe "without token" do
			before { get users_path, { :format => :json } }
			it "should be unauthorized" do
				expect(status).to eq(401)
			end
			it "is a json response" do
				expect(response.header['Content-Type']).to include 'application/json'
			end
			it "does not display the users" do
				expect(body).not_to eq([user, admin])
			end
		end
		describe "with provided token" do
			before do
				admin.create_session_token
				@params = { format: :json, token: admin.sessions.last.token }
			end
			describe "user index" do
				before { get users_path, @params }
				it "returns the users list" do
					expect(response).to render_template(:index)
				end
				it "has the correct header" do
					expect(response.header['Content-Type']).to include 'application/json'
				end
				it "should be authorized" do
					expect(status).to eq(200)
				end
			end
			describe "individual user" do
				describe "existing user" do
					before { 
						get user_path(user.id), @params
					}
					it "has the correct header" do
						expect(response.header['Content-Type']).to include 'application/json'
					end
					it "should be authorized" do
						expect(status).to eq(200)
					end
					it "shows a restricted Json" do
						expect(response).to render_template(:show)
					end
				end
				describe "non existing user" do
					it "should raise an error" do
						expect {
							get user_path( { id: 555 } ), @params
						}.to raise_error(ActiveRecord::RecordNotFound)
					end
				end
			end
		end
	end
end
