# Read about factories at https://github.com/thoughtbot/factory_girl

FactoryGirl.define do
  factory :issue do
    status "MyString"
    severity "MyString"
    short_description "MyString"
    long_description "MyString"
    component "MyString"
  end
end
