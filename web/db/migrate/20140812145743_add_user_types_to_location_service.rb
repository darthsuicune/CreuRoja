class AddUserTypesToLocationService < ActiveRecord::Migration
	def change
		add_column :location_services, :doc, :integer
		add_column :location_services, :due, :integer
		add_column :location_services, :tes, :integer
		add_column :location_services, :ci, :integer
		add_column :location_services, :asi, :integer
		add_column :location_services, :btp, :integer
		add_column :location_services, :b1, :integer
		add_column :location_services, :acu, :integer
		add_column :location_services, :per, :integer
		add_column :service_users, :user_position, :string
	end
end
