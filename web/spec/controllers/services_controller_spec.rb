require 'rails_helper'

# This spec was generated by rspec-rails when you ran the scaffold generator.
# It demonstrates how one might use RSpec to specify the controller code that
# was generated by Rails when you ran the scaffold generator.
#
# It assumes that the implementation code is generated by the rails scaffold
# generator.  If you are using any extension libraries to generate different
# controller code, this generated spec may or may not pass.
#
# It only uses APIs available in rails and/or rspec-rails.  There are a number
# of tools you can use to make these specs even more expressive, but we're
# sticking to rails and rspec-rails APIs to keep things simple and stable.
#
# Compared to earlier versions of this generator, there is very limited use of
# stubs and message expectations in this spec.  Stubs are only used when there
# is no simpler way to get a handle on the object needed for the example.
# Message expectations are only used when there is no simpler way to specify
# that an instance is receiving a specific message.

	describe ServicesController do

	# This should return the minimal set of attributes required to create a valid
	# Service. As you add validations to Service, be sure to
	# adjust the attributes here as well.
	let(:valid_attributes) { { "name" => "MyString", "assembly_id" => 1, "base_time" => Time.now,
										"start_time" => 1.minute.from_now, "end_time" => 2.minutes.from_now } }

	# This should return the minimal set of values that should be in the session
	# in order to pass any filters (e.g. authentication) defined in
	# ServicesController. Be sure to keep this updated too.
	let(:valid_session) { {} }


	describe "without signin in" do
		subject { page }
		describe "index" do
			before { get :index, {}, valid_session }
			it { should redirect_to(signin_url) }
		end
		describe "show" do
			before { get :show, {:id => 0}, valid_session }
			it { should redirect_to(signin_url) }
		end
		describe "new" do
			before { get :new, {}, valid_session }
			it { should redirect_to(signin_url) }
		end
		describe "edit" do
			before { get :edit, {:id => 0}, valid_session }
			it { should redirect_to(signin_url) }
		end
		describe "create" do
			before { post :create, {:service => valid_attributes}, valid_session }
			it { should redirect_to(signin_url) }
		end
		describe "update" do
			before { put :update, {:id => 0, :service => { "name" => "MyString" }}, valid_session }
			it { should redirect_to(signin_url) }
		end
		describe "destroy" do
			before { delete :destroy, {:id => 0}, valid_session }
			it { should redirect_to(signin_url) }
		end
	end
	
	describe "signed in" do
		let(:user) { FactoryGirl.create(:admin) }
		before { sign_in user }

		describe "GET index" do
			it "assigns all services as @services" do
				service = Service.create! valid_attributes
				get :index, {}, valid_session
				expect(assigns(:services)).to eq([service])
			end
		end

		describe "GET show" do
			it "assigns the requested service as @service" do
				service = Service.create! valid_attributes
				get :show, {:id => service.to_param}, valid_session
				expect(assigns(:service)).to eq(service)
			end
		end

		describe "GET new" do
			it "assigns a new service as @service" do
				get :new, {}, valid_session
				expect(assigns(:service)).to be_a_new(Service)
			end
		end

		describe "GET edit" do
			it "assigns the requested service as @service" do
				service = Service.create! valid_attributes
				get :edit, {:id => service.to_param}, valid_session
				expect(assigns(:service)).to eq(service)
			end
		end

		describe "POST create" do
			describe "with valid params" do
				it "creates a new Service" do
					expect {
						post :create, {:service => valid_attributes}, valid_session
					}.to change(Service, :count).by(1)
				end

				it "assigns a newly created service as @service" do
					post :create, {:service => valid_attributes}, valid_session
					expect(assigns(:service)).to be_a(Service)
					expect(assigns(:service)).to be_persisted
				end

				it "redirects to the created service" do
					post :create, {:service => valid_attributes}, valid_session
					expect(response).to redirect_to(Service.last)
				end
			end

			describe "with invalid params" do
				it "assigns a newly created but unsaved service as @service" do
					# Trigger the behavior that occurs when invalid params are submitted
					expect_any_instance_of(Service).to receive(:save).and_return(false)
					post :create, {:service => { "name" => "invalid value" }}, valid_session
					expect(assigns(:service)).to be_a_new(Service)
				end

				it "re-renders the 'new' template" do
					# Trigger the behavior that occurs when invalid params are submitted
					expect_any_instance_of(Service).to receive(:save).and_return(false)
					post :create, {:service => { "name" => "invalid value" }}, valid_session
					expect(response).to render_template("new")
				end
			end
		end

		describe "PUT update" do
			describe "with valid params" do
				it "updates the requested service" do
					service = Service.create! valid_attributes
					# Assuming there are no other services in the database, this
					# specifies that the Service created on the previous line
					# receives the :update_attributes message with whatever params are
					# submitted in the request.
					allow_any_instance_of(Service).to receive(:update).with({ "name" => "MyString" })
					put :update, {:id => service.to_param, :service => { "name" => "MyString" }}, valid_session
				end

				it "assigns the requested service as @service" do
					service = Service.create! valid_attributes
					put :update, {:id => service.to_param, :service => valid_attributes}, valid_session
					expect(assigns(:service)).to eq(service)
				end

				it "redirects to the service" do
					service = Service.create! valid_attributes
					put :update, {:id => service.to_param, :service => valid_attributes}, valid_session
					expect(response).to redirect_to(service)
				end
			end

			describe "with invalid params" do
				it "assigns the service as @service" do
					service = Service.create! valid_attributes
					# Trigger the behavior that occurs when invalid params are submitted
					expect_any_instance_of(Service).to receive(:save).and_return(false)
					put :update, {:id => service.to_param, :service => { "name" => "invalid value" }}, valid_session
					expect(assigns(:service)).to eq(service)
				end

				it "re-renders the 'edit' template" do
					service = Service.create! valid_attributes
					# Trigger the behavior that occurs when invalid params are submitted
					expect_any_instance_of(Service).to receive(:save).and_return(false)
					put :update, {:id => service.to_param, :service => { "name" => "invalid value" }}, valid_session
					expect(response).to render_template("edit")
				end
			end
		end

		describe "DELETE destroy" do
			it "destroys the requested service" do
				service = Service.create! valid_attributes
				expect {
					delete :destroy, {:id => service.to_param}, valid_session
				}.to change(Service, :count).by(-1)
			end

			it "redirects to the services list" do
				service = Service.create! valid_attributes
				delete :destroy, {:id => service.to_param}, valid_session
				expect(response).to redirect_to(services_url)
			end
		end
	end
end