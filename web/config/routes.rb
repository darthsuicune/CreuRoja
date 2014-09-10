CreuRoja::Application.routes.draw do

	# The priority is based upon order of creation: first created -> highest priority.
	# See how all your routes lay out with "rake routes".

	# You can have the root of your site routed with "root"
	root 'locations#map'

	# Example of named route that can be invoked with purchase_url(id: product.id)
	#   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase
	
	#Regular routes
	get '/home' => 'static_pages#home'
	get '/contact' => 'static_pages#contact'
	get '/about' => 'static_pages#about'
	get '/signin' => 'sessions#new'
	get '/login' => 'sessions#new'
	get '/signout' => 'sessions#destroy'
	get '/logout' => 'sessions#destroy'
	get '/map' => 'locations#map'
	post '/users/:id' => 'users#update'
	get '/email_sent' => 'static_pages#email_sent'
	get '/services/graphic' => 'services#graphic'
	get '/locations/map' => 'locations#map'

	#Resource routes (maps HTTP verbs to controller actions automatically):
	resources :users do
		resources :services
	end
	resources :sessions, only: [:new, :create, :destroy]
	resources :services do
		resources :vehicles
	end
	resources :vehicles
	resources :locations
	resources :service_users, only: [:create, :update, :destroy]
	resources :location_services, only: [:create, :update, :destroy]
	resources :location_users, only: [:create, :update, :destroy]
	resources :vehicle_services, only: [:create, :update, :destroy]
	resources :vehicle_assemblies, only: [:create, :update, :destroy]
	resources :password_reset, only: [:new, :create, :edit, :update]
	resources :vehicle_positions, only: [:index, :create]
	resources :issues

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
