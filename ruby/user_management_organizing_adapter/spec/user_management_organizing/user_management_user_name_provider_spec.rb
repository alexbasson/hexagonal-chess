require 'user_management/user_id'
require 'user_management/user'
require 'user_management/user_repository'
require 'organizing/user_name_provider'
require 'user_management_organizing/user_management_user_name_provider'

RSpec.describe UserManagementOrganizing::UserManagementUserNameProvider do
  subject(:provider) { described_class.new(user_repository) }

  let(:user_repository) { instance_double(UserManagement::UserRepository) }
  let(:user_id)         { UserManagement::UserId.new(value: 'u1') }
  let(:user)            { UserManagement::User.new(id: user_id, email: 'a@b.com', display_name: 'Alice') }

  it 'returns the display name when user exists' do
    allow(user_repository).to receive(:find_by_id).with(user_id).and_return(user)

    expect(provider.get_display_name('u1')).to eq('Alice')
  end

  it 'raises when user not found' do
    allow(user_repository).to receive(:find_by_id).and_return(nil)

    expect { provider.get_display_name('unknown') }.to raise_error(ArgumentError)
  end
end
