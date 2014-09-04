class AddExpiredateToLocations < ActiveRecord::Migration
	def change
		add_column :locations, :expiredate, :integer
	end
end
